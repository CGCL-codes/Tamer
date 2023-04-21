    protected boolean handleButtonDown(int button) {
        if (!isGraphicalViewer()) return true;
        if (button != 1) {
            setState(STATE_INVALID);
            handleInvalidInput();
        }
        if (stateTransition(STATE_INITIAL, STATE_DRAG_IN_PROGRESS)) {
            if (getCurrentInput().isControlKeyDown()) setSelectionMode(TOGGLE_MODE); else if (getCurrentInput().isShiftKeyDown()) setSelectionMode(APPEND_MODE);
        }
        return true;
    }
