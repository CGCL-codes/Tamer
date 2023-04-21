                    public void run() {
                        IFile file = ((IFileEditorInput) input).getFile();
                        if (!file.exists()) {
                            IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
                            page.closeEditor(VisualDBEditor.this, false);
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
