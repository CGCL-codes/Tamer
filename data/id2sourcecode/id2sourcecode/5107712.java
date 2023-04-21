    @Override
    public void initialize(IWorkbenchConfigurer configurer) {
        super.initialize(configurer);
        try {
            IWorkspace workspace = ResourcesPlugin.getWorkspace();
            IWorkspaceRoot wsroot = workspace.getRoot();
            IProject p = wsroot.getProject("DefaultXQZProject");
            IProjectDescription desc = workspace.newProjectDescription(p.getName());
            if (!p.exists()) {
                p.create(desc, null);
            }
            if (!p.isOpen()) {
                p.open(null);
            }
            Path dataDirPath = new Path("data");
            if (!p.getFolder(dataDirPath).exists()) {
                p.getFolder(dataDirPath).create(true, true, new NullProgressMonitor());
            }
            Enumeration<URL> genModelFiles = Activator.getDefault().getBundle().findEntries("data", "*.*", true);
            while (genModelFiles.hasMoreElements()) {
                URL urlGenModelFile = genModelFiles.nextElement();
                if (!urlGenModelFile.getFile().contains(".svn")) {
                    Path genmodelFilePath = new Path(urlGenModelFile.getPath());
                    if (!p.getFile(genmodelFilePath).exists()) {
                        p.getFile(genmodelFilePath).create(FileLocator.resolve(urlGenModelFile).openStream(), true, new NullProgressMonitor());
                    }
                }
            }
            p.refreshLocal(IResource.DEPTH_INFINITE, new NullProgressMonitor());
        } catch (CoreException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
