    @Override
    protected void performSaveAs(IProgressMonitor progressMonitor) {
        Shell shell = getSite().getShell();
        final IEditorInput input = getEditorInput();
        if (input instanceof IFileEditorInput && ((IFileEditorInput) input).getFile().isLinked() && ((IFileEditorInput) input).getFile().getProject().getName().equals(AUTOLINK_PROJECT_NAME)) {
            final IEditorInput newInput;
            IDocumentProvider provider = getDocumentProvider();
            FileDialog dialog = new FileDialog(shell, SWT.SAVE);
            IPath oldPath = URIUtil.toPath(((IURIEditorInput) input).getURI());
            if (oldPath != null) {
                dialog.setFileName(oldPath.lastSegment());
                dialog.setFilterPath(oldPath.toOSString());
            }
            dialog.setFilterExtensions(new String[] { "*.dm", "*.*" });
            String path = dialog.open();
            if (path == null) {
                if (progressMonitor != null) progressMonitor.setCanceled(true);
                return;
            }
            final File localFile = new File(path);
            if (localFile.exists()) {
                MessageDialog overwriteDialog = new MessageDialog(shell, "File Exists", null, path + " already exists. Do you wish to overwrite? ", MessageDialog.WARNING, new String[] { IDialogConstants.YES_LABEL, IDialogConstants.NO_LABEL }, 1);
                if (overwriteDialog.open() != Window.OK) {
                    if (progressMonitor != null) {
                        progressMonitor.setCanceled(true);
                        return;
                    }
                }
            }
            IFileStore fileStore;
            try {
                fileStore = EFS.getStore(localFile.toURI());
            } catch (CoreException ex) {
                MessageDialog.openError(shell, "Error", "Couldn't write file. " + ex.getMessage());
                return;
            }
            IFile file = getWorkspaceFile(fileStore);
            if (file != null) newInput = new FileEditorInput(file); else {
                IURIEditorInput uriInput = new FileStoreEditorInput(fileStore);
                java.net.URI uri = uriInput.getURI();
                IFile linkedFile = obtainLink(uri);
                newInput = new FileEditorInput(linkedFile);
            }
            if (provider == null) {
                return;
            }
            boolean success = false;
            try {
                provider.aboutToChange(newInput);
                provider.saveDocument(progressMonitor, newInput, provider.getDocument(input), true);
                success = true;
            } catch (CoreException x) {
                final IStatus status = x.getStatus();
                if (status == null || status.getSeverity() != IStatus.CANCEL) {
                    MessageDialog.openError(shell, "Error", "Couldn't write file. " + x.getMessage());
                }
            } finally {
                provider.changed(newInput);
                if (success) setInput(newInput);
            }
            if (progressMonitor != null) progressMonitor.setCanceled(!success);
            return;
        }
        super.performSaveAs(progressMonitor);
    }
