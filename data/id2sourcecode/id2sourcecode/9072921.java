            public void dragStart(DragSourceEvent event) {
                if (getEventDispatcher().isCaptured()) event.doit = false;
                if (event.doit) {
                    getEventDispatcher().dispatchNativeDragStarted(event, GraphicalViewerImpl.this);
                    flush();
                }
            }
