    @Override
    public boolean performFinish() {
        IPath root = new Path(this.creationWizardPage.getContextRoot());
        IPath out = new Path(this.creationWizardPage.getOutputDir());
        IWorkspaceRoot wr = ResourcesPlugin.getWorkspace().getRoot();
        ResourceUtil.createDir(wr, out.toString());
        final IFolder folder = wr.getFolder(out);
        if (folder == null) {
            return false;
        }
        final String errorScreenPath = "/" + out.removeFirstSegments(root.segmentCount()).toString();
        IRunnableWithProgress op = new IRunnableWithProgress() {

            @SuppressWarnings("unchecked")
            public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
                monitor.beginTask(Strings.MSG_CREATE_CONTENTS, 0);
                Bundle bundle = Activator.getDefault().getBundle();
                for (Enumeration e = bundle.findEntries("debug-info", "*", true); e.hasMoreElements(); ) {
                    URL url = (URL) e.nextElement();
                    String path = url.getPath();
                    if (path.contains(".svn") == false) {
                        IPath p = new Path(path).removeFirstSegments(1);
                        if (path.endsWith("/")) {
                            ResourceUtil.createDir(folder, p.toString());
                        } else {
                            IFile file = folder.getFile(p);
                            if (file.exists() == false && file.getName().startsWith(".") == false) {
                                try {
                                    InputStream stream = new BufferedInputStream(url.openStream());
                                    try {
                                        file.create(stream, true, monitor);
                                    } finally {
                                        stream.close();
                                    }
                                    if ("debug.jsp".equals(p.lastSegment())) {
                                        processJsp(errorScreenPath, monitor, file.getFullPath());
                                    }
                                } catch (Exception ex) {
                                    throw new InvocationTargetException(ex);
                                }
                            }
                        }
                    }
                }
            }
        };
        try {
            getContainer().run(true, false, op);
            return true;
        } catch (Exception e) {
            Activator.log(e);
        }
        return false;
    }
