package vendordep;

import org.gradle.api.DefaultTask;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.TaskAction;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;

public abstract class GenerateVendorDepTask extends DefaultTask {
    @Input
    public abstract Property<String> getDepUrl();

    private static final Gson GSON = new Gson();

    @TaskAction
    public void generate() {
        String url = getDepUrl().getOrNull();
        if (url == null || url.isEmpty()) {
            getLogger().lifecycle("Please provide the JSON URL via -PdepUrl=your_url");
            return;
        }

        try {
            parseJson(new URI(url).toURL());
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch or parse JSON", e);
        }
    }

    public void parseJson(URL url) throws Exception {
        try (InputStreamReader reader = new InputStreamReader(url.openStream())) {
            JsonObject json = GSON.fromJson(reader, JsonObject.class);

            JsonArray mavenUrls = json.getAsJsonArray("mavenUrls");
            String mavenRepoUrl = mavenUrls.get(0).getAsString();

            System.out.println("mavenRepo(\"" + mavenRepoUrl + "\")");

            JsonArray javaDeps = json.getAsJsonArray("javaDependencies");
            for (int i = 0; i < javaDeps.size(); i++) {
                JsonObject dep = javaDeps.get(i).getAsJsonObject();
                String groupId = dep.get("groupId").getAsString();
                String artifactId = dep.get("artifactId").getAsString();
                String version = dep.get("version").getAsString();

                System.out.println("vendorDep(\"" + groupId + "\", \"" + artifactId + "\", \"" + version + "\")");
            }

            JsonArray jniDeps = json.getAsJsonArray("jniDependencies");
            for (int i = 0; i < jniDeps.size(); i++) {
                JsonObject dep = jniDeps.get(i).getAsJsonObject();
                String groupId = dep.get("groupId").getAsString();
                String artifactId = dep.get("artifactId").getAsString();
                String version = dep.get("version").getAsString();
                boolean isJar = dep.get("isJar").getAsBoolean();

                System.out.println("nativeDep(\"" + groupId + "\", \"" + artifactId + "\", \"" + version + "\", wpi.platforms.desktop, " + isJar + ")");
            }
        }
    }
}
