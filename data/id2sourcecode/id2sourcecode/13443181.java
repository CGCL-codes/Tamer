    @Override
    public void run(IProgressMonitor monitor) throws InvocationTargetException {
        if (monitor == null) {
            monitor = new NullProgressMonitor();
        }
        try {
            monitor.beginTask("Creating project structure and scripts...", 3);
            Bundle bundle = PacxUiPlugin.getDefault().getBundle();
            for (Enumeration entries = bundle.findEntries("template", "*", true); entries.hasMoreElements(); ) {
                URL url = (URL) entries.nextElement();
                String path = url.getPath();
                if (!path.startsWith("/template/")) throw new InvocationTargetException(new Throwable("Unknown template file: " + path));
                String targetPath = path.substring("/template".length());
                if (path.endsWith("/")) {
                    IFolder folder = project.getFolder(targetPath);
                    if (!folder.exists()) {
                        folder.create(false, true, null);
                    }
                } else {
                    InputStream in = url.openStream();
                    IFile file = project.getFile(targetPath);
                    if (file.exists()) {
                        file.delete(false, null);
                    }
                    file.create(in, true, null);
                }
            }
            monitor.worked(1);
            createLauncherFiles();
            createFile("bin/corpus.properties", "corpus.version=2\n" + "corpus.name=" + project.getName());
            monitor.worked(1);
            IProjectDescription description = project.getDescription();
            String[] natures = new String[] { "net.sf.vex.editor.pluginNature" };
            description.setNatureIds(natures);
            project.setDescription(description, null);
            monitor.worked(1);
        } catch (IOException e) {
            throw new InvocationTargetException(e);
        } catch (CoreException e) {
            throw new InvocationTargetException(e);
        } finally {
            monitor.done();
        }
    }
