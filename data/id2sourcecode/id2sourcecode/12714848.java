    public void doSetInput(IEditorInput input, boolean releaseEditorContents) throws CoreException {
        if (input == null) close(isSaveOnCloseNeeded()); else {
            IEditorInput oldInput = getEditorInput();
            if (oldInput != null) {
                getDocumentProvider().disconnect(oldInput);
                if (releaseEditorContents) releaseInput();
            }
            updateDocumentProvider(input);
            IDocumentProvider provider = getDocumentProvider();
            if (provider == null || !(provider instanceof IDiagramDocumentProvider)) {
                IStatus s = new Status(IStatus.ERROR, EditorPlugin.getPluginId(), IStatus.OK, EditorMessages.Editor_error_no_provider, null);
                throw new CoreException(s);
            }
            if (!(input instanceof MEditingDomainElement)) {
                input = ((IDiagramDocumentProvider) provider).createInputWithEditingDomain(input, createEditingDomain());
            }
            provider.connect(input);
            try {
                super.setInput(input);
            } catch (Throwable e) {
                if (getDiagram() == null) {
                    IStatus status = provider.getStatus(input);
                    if (status != null) throw new CoreException(status); else {
                        IStatus s = new Status(IStatus.ERROR, EditorPlugin.getPluginId(), IStatus.OK, EditorMessages.Editor_error_init, null);
                        throw new CoreException(s);
                    }
                }
            }
            initializeTitle(input);
            if (oldInput != null && releaseEditorContents) initializeGraphicalViewerContents();
        }
        firePropertyChange(IEditorPart.PROP_INPUT);
    }
