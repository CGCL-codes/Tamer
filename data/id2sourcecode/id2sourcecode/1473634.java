    @DisposeProjectAfter
    public void test_getGWTLocation_otherUserName() throws Exception {
        do_projectDispose();
        do_projectCreate();
        File gwtUserFile;
        {
            gwtUserFile = File.createTempFile("gwtUser_", ".jar").getCanonicalFile();
            gwtUserFile.deleteOnExit();
            FileUtils.copyFile(new File(GTestUtils.getLocation() + "/gwt-user.jar"), gwtUserFile);
        }
        File gwtDevFile;
        {
            gwtDevFile = new File(gwtUserFile.getParentFile(), "gwt-dev.jar");
            gwtDevFile.deleteOnExit();
            FileUtils.copyFile(new File(GTestUtils.getLocation() + "/gwt-dev.jar"), gwtDevFile);
        }
        {
            IJavaProject javaProject = m_testProject.getJavaProject();
            IClasspathEntry entry = JavaCore.newLibraryEntry(new Path(gwtUserFile.getAbsolutePath()), null, null);
            ProjectUtils.addClasspathEntry(javaProject, entry);
        }
        {
            String expected = new Path(gwtUserFile.getParent()).toPortableString();
            assertEquals(expected, Utils.getGWTLocation(m_project));
        }
        {
            String expected = new Path(gwtUserFile.getAbsolutePath()).toPortableString();
            assertEquals(expected, Utils.getUserLibPath(m_project).toPortableString());
        }
        {
            {
                String expected = new Path(gwtDevFile.getAbsolutePath()).toPortableString();
                assertEquals(expected, Utils.getDevLibPath(m_project).toPortableString());
            }
            {
                gwtDevFile.delete();
                String expected = GTestUtils.getLocation() + "/gwt-dev.jar";
                assertEquals(expected, Utils.getDevLibPath(m_project).toPortableString());
            }
        }
    }
