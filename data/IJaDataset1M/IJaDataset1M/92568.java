package net.sf.buildbox.saxutil;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.junit.Test;
import org.xml.sax.SAXException;

public class ReflectiveSaxHandlerTest {

    @Test
    public void test() throws IOException, SAXException, InstantiationException, IllegalAccessException {
        final File file = new File("pom.xml");
        System.out.println("file = " + file.getAbsolutePath());
        ReflectiveSaxHandler<PomProject> handler = SaxUtils.parse(file, new ReflectiveSaxHandler<PomProject>(PomProject.class, "project"));
        PomProject project = handler.getResult();
        System.out.println("project = " + project);
        System.out.println("project.modelVersion     = " + project.modelVersion);
        System.out.println("project.parent     = " + project.parent);
        System.out.println("project.groupId    = " + project.groupId);
        System.out.println("project.artifactId = " + project.artifactId);
        System.out.println("project.version    = " + project.version);
        System.out.println("project.modules    = " + project.modules);
        System.out.println("project.dependencies=" + project.dependencies);
        System.out.println("project.properties = " + project.properties);
    }

    private static class PomGAV {

        String groupId;

        String artifactId;

        String version;

        public String toString() {
            return groupId + ":" + artifactId + ":" + version;
        }
    }

    static final class PomProject extends PomGAV {

        PomParent parent;

        String modelVersion;

        @CollInfo(item = "dependency", itemClass = PomDependency.class)
        List<PomDependency> dependencies;

        @CollInfo(item = "module")
        List<PomDependency> modules;

        String packaging;

        String name;

        String description;

        String url;

        Object dependencyManagement;

        Object build;

        Object scm;

        Map<String, String> properties;
    }

    static final class PomParent extends PomGAV {

        String relativePath;
    }

    static final class PomDependency extends PomGAV {

        String type;

        String classifier;

        String scope;

        String systemPath;

        @CollInfo(item = "exclusion", itemClass = PomExclusion.class)
        List<PomExclusion> exclusions;

        boolean optional;
    }

    static final class PomExclusion {

        String groupId;

        String artifactId;
    }
}
