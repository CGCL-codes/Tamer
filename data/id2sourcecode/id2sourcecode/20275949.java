    private File saveSignedFile(String fileDest) {
        File file = null;
        try {
            if (checkExistingFile(fileDest)) {
                boolean exportExistingDar = MessageDialog.openQuestion(new Shell(), "File location", "A signed file already exists :" + fileDest + " .\n Do you want to overwrite this signed file?");
                if (!exportExistingDar) {
                }
            }
            FileOutputStream flux = new FileOutputStream(fileDest);
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer trans = tf.newTransformer();
            if (flux != null) {
                trans.transform(new DOMSource(doc), new StreamResult(flux));
            }
            flux.flush();
            flux.close();
            IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
            root.refreshLocal(IResource.DEPTH_INFINITE, new NullProgressMonitor());
        } catch (Exception e) {
            ErrorDialog.openError(new Shell(), "Signin Plugin Error", "", new Status(IStatus.ERROR, SecurityPlugin.ID, IStatus.OK, e.getMessage(), e));
            e.printStackTrace();
        }
        return file;
    }
