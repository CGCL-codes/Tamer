    private TransferDropTargetListener createTransferDropTargetListener(final ScrollingGraphicalViewer viewer) {
        return new TemplateTransferDropTargetListener(viewer) {

            @Override
            protected CreationFactory getFactory(final Object template) {
                if (template instanceof CreationFactory) return (CreationFactory) template;
                return null;
            }
        };
    }
