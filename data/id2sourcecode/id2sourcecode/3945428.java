        public void dispose() {
            getSelectionSynchronizer().removeViewer(getViewer());
            if (getGraphicalViewer().getControl() != null && !getGraphicalViewer().getControl().isDisposed()) getGraphicalViewer().getControl().removeDisposeListener(disposeListener);
            super.dispose();
        }
