    public static final void configureProjectAsGWTProject(IJavaProject javaProject) throws Exception {
        IProject project = javaProject.getProject();
        if (!ProjectUtils.hasType(javaProject, "com.google.gwt.core.client.GWT")) {
            IClasspathEntry entry;
            if (Utils.hasGPE()) {
                entry = JavaCore.newContainerEntry(new Path("com.google.gwt.eclipse.core.GWT_CONTAINER"));
            } else {
                entry = JavaCore.newVariableEntry(new Path("GWT_HOME/gwt-user.jar"), null, null);
            }
            ProjectUtils.addClasspathEntry(javaProject, entry);
        }
        {
            ProjectUtils.addNature(project, Constants.NATURE_ID);
            if (Utils.hasGPE()) {
                ProjectUtils.addNature(project, "com.google.gwt.eclipse.core.gwtNature");
            }
        }
        {
            String webFolderName = WebUtils.getWebFolderName(project);
            ensureCreateFolder(project, webFolderName);
            ensureCreateFolder(project, webFolderName + "/WEB-INF");
            IFolder classesFolder = ensureCreateFolder(project, webFolderName + "/WEB-INF/classes");
            IFolder libFolder = ensureCreateFolder(project, webFolderName + "/WEB-INF/lib");
            javaProject.setOutputLocation(classesFolder.getFullPath(), null);
            if (!libFolder.getFile("gwt-servlet.jar").exists()) {
                String servletJarLocation = Utils.getGWTLocation(project) + "/gwt-servlet.jar";
                File srcFile = new File(servletJarLocation);
                File destFile = new File(libFolder.getLocation().toFile(), "gwt-servlet.jar");
                FileUtils.copyFile(srcFile, destFile);
                libFolder.refreshLocal(IResource.DEPTH_INFINITE, null);
            }
        }
    }
