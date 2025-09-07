package vendordep;

import com.google.gson.*;
import org.gradle.api.DefaultTask;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.TaskAction;

import java.io.InputStreamReader;
import java.net.URL;

public abstract class GenerateVendorDepTask extends DefaultTask {
    @Input
    public abstract Property<String> getDepUrl();

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    @TaskAction
    public void generate() {
        String url = getDepUrl().getOrNull();
        if (url == null || url.isEmpty()) {
            getLogger().lifecycle("Please provide the JSON URL via -PdepUrl=your_url");
            return;
        }

        try (InputStreamReader reader = new InputStreamReader(new URL(url).openStream())) {
            JsonObject json = GSON.fromJson(reader, JsonObject.class);

            // Start vendorDep block
            System.out.println("vendorDep {");

            // Repos
            JsonArray mavenUrls = json.getAsJsonArray("mavenUrls");
            for (JsonElement elem : mavenUrls) {
                String repoUrl = elem.getAsString();
                System.out.println("    mavenRepo(\"" + repoUrl + "\")");
            }

            // Java deps
            JsonArray javaDeps = json.getAsJsonArray("javaDependencies");
            for (JsonElement elem : javaDeps) {
                JsonObject dep = elem.getAsJsonObject();
                String groupId = dep.get("groupId").getAsString();
                String artifactId = dep.get("artifactId").getAsString();
                String version = dep.get("version").getAsString();
                System.out.println("    vendorDep(\"" + groupId + "\", \"" + artifactId + "\", \"" + version + "\")");
            }

            // JNI deps
            JsonArray jniDeps = json.getAsJsonArray("jniDependencies");
            for (JsonElement elem : jniDeps) {
                JsonObject dep = elem.getAsJsonObject();
                String groupId = dep.get("groupId").getAsString();
                String artifactId = dep.get("artifactId").getAsString();
                String version = dep.get("version").getAsString();
                boolean isJar = dep.get("isJar").getAsBoolean();
                // For now just hardcode desktop like your Groovy version. Replace with more specific later
                System.out.println("    nativeDep(\"" + groupId + "\", \"" + artifactId + "\", \"" + version + "\", wpi.platforms.desktop, " + isJar + ")");
            }

            // End vendorDep block
            System.out.println("}");

        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch or parse vendordep JSON", e);
        }
    }
}
