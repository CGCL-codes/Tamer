    @DisposeProjectAfter
    public void test_gwtInMavenStructure() throws Exception {
        ProjectUtils.removeClasspathEntries(m_javaProject, new Predicate<IClasspathEntry>() {

            @Override
            public boolean apply(IClasspathEntry entry) {
                return entry.getPath().toPortableString().contains("gwt-");
            }
        });
        File userFile;
        File devFile;
        {
            String testLocation = Activator.getDefault().getStateLocation().toPortableString();
            File gwtDirectory = new File(testLocation + "/SDK from Maven");
            userFile = new File(gwtDirectory + "/gwt/gwt-user/2.2.0/gwt-user-2.2.0.jar");
            devFile = new File(gwtDirectory + "/gwt/gwt-dev/2.2.0/gwt-dev-2.2.0.jar");
            FileUtils.forceMkdir(userFile.getParentFile());
            FileUtils.forceMkdir(devFile.getParentFile());
            String sdkLocation = getGWTLocation_forProject();
            FileUtils.copyFile(new File(sdkLocation + "/gwt-user.jar"), userFile, false);
            FileUtils.copyFile(new File(sdkLocation + "/gwt-dev.jar"), devFile, false);
            ProjectUtils.addExternalJar(m_javaProject, userFile.getAbsolutePath(), null);
            m_project.refreshLocal(IResource.DEPTH_INFINITE, null);
        }
        try {
            parseJavaInfo("// filler filler filler filler filler", "public class Test extends FlowPanel {", "  public Test() {", "  }", "}");
        } finally {
            makeGwtJarEmpty(userFile);
            makeGwtJarEmpty(devFile);
        }
    }
