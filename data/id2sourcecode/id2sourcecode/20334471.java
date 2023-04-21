    protected void handleFilesDropped(DropTargetEvent event) {
        String[] files = (String[]) event.data;
        if (files != null) {
            File targetParentFolder = null;
            if (event.item == null) {
                targetParentFolder = _rootFolder;
            } else if (event.item instanceof TreeItem) {
                TreeItem treeItem = (TreeItem) event.item;
                targetParentFolder = (File) treeItem.getData();
                if (!targetParentFolder.isDirectory()) {
                    targetParentFolder = targetParentFolder.getParentFile();
                }
            }
            if (targetParentFolder != null && targetParentFolder.exists() && targetParentFolder.isDirectory()) {
                try {
                    StrakerFileUtils.copyFilesWithProgressMonitor(targetParentFolder, files, getControl().getShell());
                } catch (IOException ex) {
                    MessageDialog.openError(getControl().getShell(), "Copy Files", ex.getMessage());
                }
                refresh(targetParentFolder);
                expandToLevel(targetParentFolder, 1);
                setSelection(new StructuredSelection(targetParentFolder));
            }
        }
    }
