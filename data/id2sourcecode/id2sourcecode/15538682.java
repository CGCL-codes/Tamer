    protected boolean handleButtonDown(int button) {
        if (!stateTransition(STATE_INITIAL, STATE_DRAG)) {
            resetHover();
            return true;
        }
        resetHover();
        EditPartViewer viewer = getCurrentViewer();
        Point p = getLocation();
        if (getDragTracker() != null) getDragTracker().deactivate();
        if (viewer instanceof GraphicalViewer) {
            Handle handle = ((GraphicalViewer) viewer).findHandleAt(p);
            if (handle != null) {
                setDragTracker(handle.getDragTracker());
                return true;
            }
        }
        updateTargetRequest();
        ((SelectionRequest) getTargetRequest()).setLastButtonPressed(button);
        updateTargetUnderMouse();
        EditPart editpart = getTargetEditPart();
        if (editpart != null) {
            setDragTracker(editpart.getDragTracker(getTargetRequest()));
            lockTargetEditPart(editpart);
            return true;
        }
        return false;
    }
