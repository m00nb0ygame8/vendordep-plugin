package vendordep;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.tasks.TaskContainer;

public class VendorDepPlugin implements Plugin<Project> {
    @Override
    public void apply(Project project) {
        VendorDepExtension ext = project.getExtensions()
                .create("vendorDep", VendorDepExtension.class);

        project.afterEvaluate(p -> {

            for (String repo : ext.mavenRepos) {
                project.getRepositories().maven(r -> r.setUrl(repo));
            }
            for (String impl : ext.impls) {
                project.getDependencies().add("implementation", impl);
            }
            for (String rt : ext.runtimes) {
                project.getDependencies().add("runtimeOnly", rt);
            }

            Configuration vendorConfig = project.getConfigurations().maybeCreate("vendorDeps");
            for (String dep : ext.impls) {
                project.getDependencies().add("vendorDeps", dep);
            }
            for (String dep : ext.runtimes) {
                project.getDependencies().add("vendorDeps", dep);
            }

            TaskContainer tasks = project.getTasks();
            tasks.register("prefetchVendorDeps", task -> {
                task.getInputs().files(vendorConfig);
                task.doLast(t -> {
                    vendorConfig.getFiles().forEach(file ->
                           project.getLogger().lifecycle("Prefetched: " + file.getName()));
                });
            });

        });

    }
}
