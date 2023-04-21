    @DisposeProjectAfter
    public void test_gwtInDirectoryWithSpace() throws Exception {
        ProjectUtils.removeClasspathEntries(m_javaProject, new Predicate<IClasspathEntry>() {

            @Override
            public boolean apply(IClasspathEntry entry) {
                return entry.getPath().toPortableString().contains("gwt-");
            }
        });
        File gwtDirectory;
        {
            String testLocation = Activator.getDefault().getStateLocation().toPortableString();
            gwtDirectory = new File(testLocation + "/SDK with spaces");
            String sdkLocation = getGWTLocation_forProject();
            FileUtils.copyFileToDirectory(new File(sdkLocation + "/gwt-user.jar"), gwtDirectory);
            FileUtils.copyFileToDirectory(new File(sdkLocation + "/gwt-dev.jar"), gwtDirectory);
            m_testProject.addExternalJars(gwtDirectory.getAbsolutePath());
        }
        try {
            parseJavaInfo("// filler filler filler filler filler", "public class Test extends FlowPanel {", "  public Test() {", "  }", "}");
        } finally {
            makeGwtJarsEmpty(gwtDirectory);
        }
    }
