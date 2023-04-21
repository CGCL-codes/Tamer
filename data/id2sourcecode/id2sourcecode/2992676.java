    protected void navigateFocusedComp(int direction) {
        int nComp = toolBar.getComponentCount();
        int j;
        switch(direction) {
            case EAST:
            case SOUTH:
                if (focusedCompIndex < 0 || focusedCompIndex >= nComp) {
                    break;
                }
                j = focusedCompIndex + 1;
                while (j != focusedCompIndex) {
                    if (j >= nComp) {
                        j = 0;
                    }
                    Component comp = toolBar.getComponentAtIndex(j++);
                    if (comp != null && comp.isFocusable() && comp.isEnabled()) {
                        comp.requestFocus();
                        break;
                    }
                }
                break;
            case WEST:
            case NORTH:
                if (focusedCompIndex < 0 || focusedCompIndex >= nComp) {
                    break;
                }
                j = focusedCompIndex - 1;
                while (j != focusedCompIndex) {
                    if (j < 0) {
                        j = nComp - 1;
                    }
                    Component comp = toolBar.getComponentAtIndex(j--);
                    if (comp != null && comp.isFocusable() && comp.isEnabled()) {
                        comp.requestFocus();
                        break;
                    }
                }
                break;
            default:
                break;
        }
    }
