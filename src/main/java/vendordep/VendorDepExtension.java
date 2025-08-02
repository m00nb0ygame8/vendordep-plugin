package vendordep;

import java.util.ArrayList;
import java.util.List;

public class VendorDepExtension {
    public final List<String> mavenRepos, impls, runtimes;

    public VendorDepExtension() {
        this.mavenRepos = new ArrayList<>();
        this.impls = new ArrayList<>();
        this.runtimes = new ArrayList<>();
    }

    public void mavenRepo(String mavenRepo) {
        mavenRepos.add(mavenRepo);
    }

    public void vendorDep(String group, String artifact, String version) {
        impls.add(group + ":" + artifact + ":" + version);
    }

    public void implementation(String dep) {
        impls.add(dep);
    }

    public void nativeDep(String dep) {
        runtimes.add(dep);
    }


}
