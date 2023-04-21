        public void dispose() {
            if (getGraphicalViewer().getControl() != null && !getGraphicalViewer().getControl().isDisposed()) {
                getGraphicalViewer().getControl().removeDisposeListener(disposeListener);
            }
        }
