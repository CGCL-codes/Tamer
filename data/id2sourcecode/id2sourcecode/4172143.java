    @Override
    public boolean close() {
        if (myThumbnail != null) {
            myThumbnail.deactivate();
            myThumbnail = null;
        }
        if ((myGraphicalViewer.getControl() != null) && !myGraphicalViewer.getControl().isDisposed()) {
            myGraphicalViewer.getControl().getShell().removeControlListener(myShellMoveListener);
            myGraphicalViewer.getControl().removeControlListener(myParentResizeListener);
        }
        myCanvas.removeControlListener(myResizeListener);
        myCanvas.removeFocusListener(myFocusAdapter);
        hideTracker();
        myCloseLocation = getShell().getLocation();
        myCloseSize = getShell().getSize();
        return super.close();
    }
