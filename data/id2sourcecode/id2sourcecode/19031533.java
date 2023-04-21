    public void mouseEvent(MouseEvent e, Point screen, Point model) {
        if (hidden) return;
        Point p1;
        if (useCameraCoordinates) p1 = model; else p1 = screen;
        Point pt = new Point(p1.x, p1.y);
        if (e.getID() != MouseEvent.MOUSE_DRAGGED) {
            mouseDragging = false;
            if (c.focus().isFocused(this) && c.focus().isModal()) {
                c.focus().removeFromFocus(this);
                c.focus().setFocus(this);
            }
        }
        if (e.getID() == MouseEvent.MOUSE_MOVED || e.getID() == MouseEvent.MOUSE_RELEASED || e.getID() == MouseEvent.MOUSE_ENTERED || e.getID() == MouseEvent.MOUSE_EXITED) {
            if (withinInnerRect(pt) || mouseDragging) {
                UIUtils.setCursor(this, p, Cursor.TEXT_CURSOR);
            } else {
                UIUtils.releaseCursor(this, p);
            }
            return;
        }
        if (e.isPopupTrigger()) return;
        if (withinOuterRect(pt) || mouseDragging) {
            if (withinInnerRect(pt) || mouseDragging) {
                c.focus().setFocus(this);
                int insertionIndex = viewLo;
                float ult = x + getPosForIndex(viewLo);
                float penult = ult;
                for (int i = viewLo; i <= viewHi; i++) {
                    float pos = x + getPosForIndex(i);
                    insertionIndex = i;
                    penult = ult;
                    ult = pos;
                    if (pos > pt.x) {
                        break;
                    }
                }
                float middle = (ult + penult) / 2;
                if (pt.x < middle) insertionIndex--;
                int diff = insertionIndex - caret;
                if (e.getID() == MouseEvent.MOUSE_DRAGGED) {
                    mouseDragging = true;
                    c.focus().setModalFocus(this);
                    mouseDragPos = pt.x;
                    if (insertionIndex > 1 && insertionIndex < text.length() - 1) handleDragScroll();
                    selectChar(diff);
                } else if (e.getID() == MouseEvent.MOUSE_PRESSED) {
                    switch(e.getClickCount()) {
                        case (1):
                            if (shiftPressed) selectChar(diff); else moveChar(diff);
                            break;
                        case (2):
                            moveChar(diff);
                            nextWord(MOVE, RIGHT, false);
                            nextWord(SELECT, LEFT, false);
                            break;
                        case (3):
                            selectAll();
                            break;
                    }
                }
            }
        } else if (e.getID() == MouseEvent.MOUSE_PRESSED) {
            if (c.focus().removeFromFocus(this)) clearSelection();
        }
    }
