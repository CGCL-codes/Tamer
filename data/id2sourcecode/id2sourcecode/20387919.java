    @Override
    protected void performSaveAs(IProgressMonitor progressMonitor) {
        Shell shell = getSite().getShell();
        final IEditorInput input = getEditorInput();
        if (input instanceof IFileEditorInput && ((IFileEditorInput) input).getFile().isLinked() && ((IFileEditorInput) input).getFile().getProject().getName().equals(ExtLinkedFileHelper.AUTOLINK_PROJECT_NAME)) {
            final IEditorInput newInput;
            IDocumentProvider provider = getDocumentProvider();
            String suggestedName = null;
            String suggestedPath = null;
            {
                java.net.URI uri = ((IURIEditorInput) input).getURI();
                String tmpProperty = null;
                try {
                    tmpProperty = ((IFileEditorInput) input).getFile().getPersistentProperty(TmpFileStoreEditorInput.UNTITLED_PROPERTY);
                } catch (CoreException e) {
                }
                boolean isUntitled = tmpProperty != null && "true".equals(tmpProperty);
                IPath oldPath = URIUtil.toPath(uri);
                suggestedName = isUntitled ? input.getName() : oldPath.lastSegment();
                try {
                    suggestedPath = isUntitled ? ((IFileEditorInput) input).getFile().getWorkspace().getRoot().getPersistentProperty(LAST_SAVEAS_LOCATION) : oldPath.toOSString();
                } catch (CoreException e) {
                }
                if (suggestedPath == null) {
                    suggestedPath = System.getProperty("user.home");
                }
            }
            FileDialog dialog = new FileDialog(shell, SWT.SAVE);
            if (suggestedName != null) dialog.setFileName(suggestedName);
            if (suggestedPath != null) dialog.setFilterPath(suggestedPath);
            dialog.setFilterExtensions(new String[] { "*.b3", "*.*" });
            String path = dialog.open();
            if (path == null) {
                if (progressMonitor != null) progressMonitor.setCanceled(true);
                return;
            }
            final File localFile = new File(path);
            if (localFile.exists()) {
                MessageDialog overwriteDialog = new MessageDialog(shell, "Save As", null, path + " already exists.\nDo you want to replace it?", MessageDialog.WARNING, new String[] { IDialogConstants.YES_LABEL, IDialogConstants.NO_LABEL }, 1);
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
                EditorsPlugin.log(ex.getStatus());
                String title = "Problems During Save As...";
                String msg = "Save could not be completed. " + ex.getMessage();
                MessageDialog.openError(shell, title, msg);
                return;
            }
            IFile file = getWorkspaceFile(fileStore);
            if (file != null) newInput = new FileEditorInput(file); else {
                IURIEditorInput uriInput = new FileStoreEditorInput(fileStore);
                java.net.URI uri = uriInput.getURI();
                IFile linkedFile = ExtLinkedFileHelper.obtainLink(uri, false);
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
                    String title = "Problems During Save As...";
                    String msg = "Save could not be completed. " + x.getMessage();
                    MessageDialog.openError(shell, title, msg);
                }
            } finally {
                provider.changed(newInput);
                if (success) setInput(newInput);
                ExtLinkedFileHelper.unlinkInput(((IFileEditorInput) input));
                String lastLocation = URIUtil.toPath(((FileEditorInput) newInput).getURI()).toOSString();
                try {
                    ((FileEditorInput) newInput).getFile().getWorkspace().getRoot().setPersistentProperty(LAST_SAVEAS_LOCATION, lastLocation);
                } catch (CoreException e) {
                }
            }
            if (progressMonitor != null) progressMonitor.setCanceled(!success);
            return;
        }
        super.performSaveAs(progressMonitor);
    }
