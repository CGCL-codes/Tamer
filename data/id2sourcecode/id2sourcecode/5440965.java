    public void hookDropTargetListener(GraphicalViewer viewer) {
        viewer.addDropTargetListener(new TransferDropTargetListener() {

            public void dragEnter(DropTargetEvent event) {
            }

            public void dragLeave(DropTargetEvent event) {
            }

            public void dragOperationChanged(DropTargetEvent event) {
            }

            public void dragOver(DropTargetEvent event) {
            }

            public void drop(DropTargetEvent event) {
            }

            public void dropAccept(DropTargetEvent event) {
            }

            public Transfer getTransfer() {
                return TemplateTransfer.getInstance();
            }

            public boolean isEnabled(DropTargetEvent event) {
                if (isInState(STATE_EXPANDED)) setState(STATE_COLLAPSED);
                return false;
            }
        });
    }
