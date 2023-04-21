    public void modelReloaded(ResourceInfo resourceInfo) {
        Resource bpelResource = fDesignViewer.getEditModelClient().getPrimaryResourceInfo().getResource();
        IFile file = getFileInput();
        BPELReader reader = new BPELReader();
        reader.read(bpelResource, file, fDesignViewer.getResourceSet());
        process = reader.getProcess();
        if (getEditDomain() != null) {
            ((BPELEditDomain) getEditDomain()).setProcess(getProcess());
        }
        extensionMap = reader.getExtensionMap();
        modelListenerAdapter.setExtensionMap(fDesignViewer.getExtensionMap());
        fDesignViewer.getGraphicalViewer().setContents(getProcess());
        updateMarkersHard();
    }
