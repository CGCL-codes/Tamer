    public void resourceChanged(final IResourceChangeEvent event) {
        if (event.getType() == IResourceChangeEvent.POST_CHANGE) {
            final IEditorInput input = getEditorInput();
            if (input instanceof IFileEditorInput) {
                Display.getDefault().asyncExec(new Runnable() {

                    public void run() {
                        IFile file = ((IFileEditorInput) input).getFile();
                        if (!file.exists()) {
                            IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
                            page.closeEditor(DiagramEditor.this, false);
                        } else {
                            if (!getPartName().equals(file.getName())) {
                                setPartName(file.getName());
                            }
                            if (needViewerRefreshFlag) {
                                refreshGraphicalViewer();
                            } else {
                                needViewerRefreshFlag = true;
                            }
                        }
                    }
                });
            }
        }
    }
