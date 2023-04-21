    protected boolean handleMove() {
        if (stateTransition(STATE_ACCESSIBLE_DRAG, STATE_INITIAL)) setDragTracker(null);
        if (isInState(STATE_INITIAL)) {
            updateTargetRequest();
            updateTargetUnderMouse();
            showTargetFeedback();
            return true;
        } else if (isInState(STATE_TRAVERSE_HANDLE)) {
            EditPartViewer viewer = getCurrentViewer();
            if (viewer instanceof GraphicalViewer) {
                Handle handle = ((GraphicalViewer) viewer).findHandleAt(getLocation());
                if (handle != null) {
                    setState(STATE_ACCESSIBLE_DRAG);
                    setStartLocation(getLocation());
                    setDragTracker(handle.getDragTracker());
                    return true;
                } else {
                    setState(STATE_INITIAL);
                }
            }
        }
        return false;
    }
