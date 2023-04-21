    protected void processKeyEvent(KeyEvent e) {
        if (e.getID() == KeyEvent.KEY_PRESSED) {
            boolean shift = (e.getModifiers() & KeyEvent.SHIFT_MASK) != 0;
            switch(e.getKeyCode()) {
                case KeyEvent.VK_HOME:
                    setSelection(0, shift);
                    break;
                case KeyEvent.VK_END:
                    setSelection(mem.getAddressSize() - 1, shift);
                    break;
                case KeyEvent.VK_LEFT:
                    setSelection(selected - 1, shift);
                    break;
                case KeyEvent.VK_RIGHT:
                    setSelection(selected + 1, shift);
                    break;
                case KeyEvent.VK_UP:
                    if (selected > 15) setSelection(selected - 16, shift);
                    break;
                case KeyEvent.VK_DOWN:
                    if (selected < mem.getAddressSize() - 16) setSelection(selected + 16, shift);
                    break;
                case KeyEvent.VK_PAGE_DOWN:
                    setSelection(selected + getPageSize(), shift);
                    break;
                case KeyEvent.VK_PAGE_UP:
                    setSelection(selected - getPageSize(), shift);
                    break;
                case KeyEvent.VK_TAB:
                    if ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0) setTextMode(!textMode);
                    break;
            }
        } else if (e.getID() == KeyEvent.KEY_TYPED) {
            char ch = e.getKeyChar();
            repaint(getRect(selected));
            if (textMode) {
                if (ch >= ' ' && ch < (char) 127) ;
                mem.writeByte(selected, (int) ch);
                selStart--;
                setAddress(Math.min(mem.getAddressSize() - 1, selected + 1));
            } else {
                int hex = "0123456789ABCDEFabcdef".indexOf(ch);
                if (hex != -1) {
                    if (hex > 15) hex -= 6;
                    if (right = !right) mem.writeByte(selected, hex); else {
                        mem.writeByte(selected, mem.readByte(selected) << 4 | hex);
                        selStart--;
                        setAddress(Math.min(mem.getAddressSize() - 1, selected + 1));
                    }
                }
            }
            repaint(getRect(selected));
        }
        cursor = counter != null;
        super.processKeyEvent(e);
    }
