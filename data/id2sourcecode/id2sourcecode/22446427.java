    public void addOrUpdateSaflets(IProject project, final List<Saflet> saflets, final boolean update, final boolean interactive) throws CoreException {
        final List<Saflet> safletsCopy = new ArrayList<Saflet>(saflets);
        project.accept(new IResourceVisitor() {

            @Override
            public boolean visit(IResource resource) throws CoreException {
                if (resource.getType() == IResource.FILE && "saflet".equals(resource.getFileExtension())) {
                    boolean skipAll = false, overwriteAll = false;
                    int pid = SafletPersistenceManager.getInstance().getResourceId(resource);
                    String existingName = SafletPersistenceManager.getInstance().getSafletName(resource);
                    for (Saflet saflet : saflets) {
                        final boolean sameName = StringUtils.equals(existingName, saflet.getName());
                        if ((pid == saflet.getId() && pid != -1) || sameName) {
                            safletsCopy.remove(saflet);
                            if (!update) continue;
                            if (interactive) {
                                if (skipAll) continue;
                                if (!overwriteAll) {
                                    String dialogMessage = null;
                                    if (sameName) dialogMessage = "A Saflet with name " + saflet.getName() + " already exists in the workspace. Do you wish to skip or overwrite? "; else dialogMessage = "Saflet (" + saflet.getName() + ") exists with the same ID a different name in the workspace (" + existingName + "). Do you wish to skip or overwrite? ";
                                    MessageDialog dlg = new MessageDialog(SafiWorkshopEditorUtil.getActiveShell(), "Overwrite Existing Saflet?", null, dialogMessage, MessageDialog.QUESTION, new String[] { "Skip", "Skip all", "Overwrite", "Overwrite All" }, 4);
                                    int result = dlg.open();
                                    switch(result) {
                                        case 0:
                                            continue;
                                        case 1:
                                            skipAll = true;
                                            continue;
                                        case 2:
                                            break;
                                        case 3:
                                            overwriteAll = true;
                                            break;
                                    }
                                }
                            }
                            IPath fullPath = null;
                            try {
                                fullPath = SafletPersistenceManager.getInstance().writeSafletToExistingFile((IFile) resource, saflet);
                                AsteriskDiagramEditor editor = getOpenEditor((IFile) resource);
                                if (editor != null) {
                                    AsteriskDiagramEditorPlugin.getDefault().getWorkbench().getActiveWorkbenchWindow().getActivePage().closeEditor(editor, false);
                                    editor = (AsteriskDiagramEditor) SafiWorkshopEditorUtil.openDiagram(URI.createFileURI(fullPath.toPortableString()), false, true);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                MessageDialog.openError(SafiWorkshopEditorUtil.getActiveShell(), "Update Error", "Couldn't update Saflet: " + e.getLocalizedMessage());
                                AsteriskDiagramEditorPlugin.getInstance().logError("Couldn't update Saflet", e);
                                break;
                            }
                        }
                    }
                }
                return true;
            }
        });
        for (Saflet saflet : safletsCopy) {
            String filename = SafiWorkshopEditorUtil.getUniqueFileName(project, saflet.getName(), "saflet");
            IFile file = project.getFile(filename);
            try {
                byte[] code = saflet.getCode() == null ? DBManager.getInstance().getSafletCode(saflet.getId()) : (saflet.getCode() == null ? null : saflet.getCode());
                file.create(new ByteArrayInputStream(code), true, null);
                Date now = new Date();
                file.setPersistentProperty(RES_ID_KEY, String.valueOf(saflet.getId()));
                file.setPersistentProperty(MODIFIED_KEY, String.valueOf(now.getTime()));
                file.setPersistentProperty(UPDATED_KEY, String.valueOf(now.getTime()));
                file.setPersistentProperty(SAFLET_NAME_KEY, saflet.getName());
            } catch (DBManagerException e) {
                throw new CoreException(new Status(IStatus.ERROR, AsteriskDiagramEditorPlugin.ID, "Couldn't write Saflet to local file", e));
            }
        }
        updateLocalProject(project, saflets.get(0).getProject());
    }
