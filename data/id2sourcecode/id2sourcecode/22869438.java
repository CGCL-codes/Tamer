    @Override
    public boolean performFinish() {
        selectedProject = selectProject.getSelectedProject();
        if (selectedProject == null) return false;
        boolean yesToAll = false;
        for (final File f : selectPage.getSaflets()) {
            final IResource[] found = new IResource[1];
            try {
                selectedProject.accept(new IResourceVisitor() {

                    @Override
                    public boolean visit(IResource resource) throws CoreException {
                        if (resource.getType() == IResource.FILE && "saflet".equals(resource.getFileExtension()) && resource.getName().equals(f.getName())) {
                            found[0] = resource;
                        }
                        return true;
                    }
                });
            } catch (CoreException e) {
                e.printStackTrace();
            }
            if (found[0] != null) {
                if (!yesToAll) {
                    MessageDialog dlg = new MessageDialog(getShell(), "Saflet Exists", null, "Saflet with name " + found[0].getName() + " exists. Do you wish to overwrite? ", MessageDialog.QUESTION, new String[] { "Yes to all", "Yes", "No", "Cancel" }, 3);
                    int result = dlg.open();
                    if (result == 0) yesToAll = true; else if (result == 2) continue; else if (result == 3) return false;
                }
                final DeleteResourcesOperation op = new DeleteResourcesOperation(new IResource[] { found[0] }, "Delete Resources Operation", true);
                try {
                    PlatformUI.getWorkbench().getOperationSupport().getOperationHistory().execute(op, new NullProgressMonitor(), WorkspaceUndoUtil.getUIInfoAdapter(null));
                } catch (ExecutionException e) {
                    e.printStackTrace();
                    MessageDialog.openError(getShell(), "Couldn't Delete Resource", "Couldn't delete resource " + found[0].getName() + ": " + e.getLocalizedMessage());
                    return false;
                }
                IPath path = ResourcesPlugin.getWorkspace().getRoot().getLocation().append(found[0].getFullPath());
                try {
                    FileUtils.copyFile(f, new File(path.toOSString()));
                } catch (IOException e) {
                    e.printStackTrace();
                    MessageDialog.openError(getShell(), "Couldn't Copy Resource", "Couldn't copy resource from " + f + ": " + e.getLocalizedMessage());
                    return false;
                }
            } else {
                IPath path = selectedProject.getLocation();
                try {
                    String pathname = path.toOSString() + File.separatorChar + f.getName();
                    FileUtils.copyFile(f, new File(pathname));
                } catch (IOException e) {
                    e.printStackTrace();
                    MessageDialog.openError(getShell(), "Couldn't Copy Resource", "Couldn't copy resource from " + f + ": " + e.getLocalizedMessage());
                    return false;
                }
            }
        }
        try {
            selectedProject.refreshLocal(IResource.DEPTH_ONE, new NullProgressMonitor());
        } catch (CoreException e) {
            e.printStackTrace();
        }
        return true;
    }
