    private TransferDropTargetListener createTransferDropTargetListener() {
        return new TemplateTransferDropTargetListener(this.getGraphicalViewer()) {

            @Override
            protected CreationFactory getFactory(Object template) {
                return new SimpleFactory((Class) template);
            }
        };
    }
