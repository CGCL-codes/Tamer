    private void closeWindow(String input) {
        Window win = (Window) BaseWindow.getInstance().pane.getSelectedComponent();
        Type type = win.getDocument().getType();
        if (type == Type.MAIN) {
        } else if (type == Type.CHANNEL) {
            String msg = "Leaving";
            if (!input.toLowerCase().matches("^/window close\\s*$")) {
                msg = input.substring(12);
            }
            win.getDocument().getSession().partChannel(win.getDocument().getChannel(), msg);
            BaseWindow.getWindowList().remove(win);
            BaseWindow.getInstance().pane.remove(win);
        } else {
            BaseWindow.getWindowList().remove(win);
            BaseWindow.getInstance().pane.remove(win);
        }
    }
