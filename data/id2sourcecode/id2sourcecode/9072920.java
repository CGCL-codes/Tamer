    protected void setDragSource(DragSource source) {
        super.setDragSource(source);
        class TheLastListener extends DragSourceAdapter {

            public void dragStart(DragSourceEvent event) {
                if (getEventDispatcher().isCaptured()) event.doit = false;
                if (event.doit) {
                    getEventDispatcher().dispatchNativeDragStarted(event, GraphicalViewerImpl.this);
                    flush();
                }
            }

            public void dragFinished(DragSourceEvent event) {
                getEventDispatcher().dispatchNativeDragFinished(event, GraphicalViewerImpl.this);
            }
        }
        if (source != null) getDragSource().addDragListener(new TheLastListener());
    }
