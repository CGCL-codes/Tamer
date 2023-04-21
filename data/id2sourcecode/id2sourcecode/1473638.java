    @DisposeProjectAfter
    public void test_getDevLibPath_maven() throws Exception {
        do_projectDispose();
        do_projectCreate();
        String gwtLocation = GTestUtils.getLocation_22();
        String gwtUserDir = getFolder("libs/gwt/gwt-user/2.2.0").getLocation().toPortableString();
        String gwtDevDir = getFolder("libs/gwt/gwt-dev/2.2.0").getLocation().toPortableString();
        String userLocation = gwtUserDir + "/gwt-user-2.2.0.jar";
        String devLocation = gwtDevDir + "/gwt-dev-2.2.0.jar";
        FileUtils.copyFile(new File(gwtLocation, "gwt-user.jar"), new File(userLocation), false);
        FileUtils.copyFile(new File(gwtLocation, "gwt-dev.jar"), new File(devLocation), false);
        ProjectUtils.addExternalJar(m_javaProject, userLocation, null);
        m_project.refreshLocal(IResource.DEPTH_INFINITE, null);
        assertEquals(devLocation, Utils.getDevLibPath(m_project).toPortableString());
    }
