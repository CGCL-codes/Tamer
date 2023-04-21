    public void buttonPressed(int code) {
        if (code >= ButtonPressedEvent.NUM0 && code <= ButtonPressedEvent.NUM9) {
            int dig = code - ButtonPressedEvent.NUM0;
            tvControls.getChannelSelector().appendDigit(dig);
            return;
        }
        XletContextImpl ctx = (XletContextImpl) this.proxy.getXletContext();
        long stamp = System.currentTimeMillis();
        KeyEvent event = new KeyEvent(ctx.getContainer(), 0, stamp, 0, toHaviCode(code), toCharCode(code));
        Container cont = TVContainer.getRootContainer(ctx);
        Component[] child = cont.getComponents();
        for (int i = 0; i < child.length; i++) {
            KeyListener[] lists = child[i].getKeyListeners();
            for (int j = 0; j < lists.length; j++) {
                if (lists[j] instanceof CommandListener) {
                    lists[j].keyPressed(event);
                }
            }
        }
    }
