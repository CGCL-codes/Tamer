    private void doXmlReload(boolean force) {
        if (force || mNeedsXmlReload) {
            GraphicalViewer viewer = getGraphicalViewer();
            SelectionManager selMan = viewer.getSelectionManager();
            ISelection selection = selMan.getSelection();
            try {
                viewer.setContents(getModel());
            } finally {
                selMan.setSelection(selection);
            }
            mNeedsXmlReload = false;
        }
    }
