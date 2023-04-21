    private void names(String input) {
        Window win = (Window) BaseWindow.getInstance().pane.getSelectedComponent();
        Channel chan = win.getDocument().getChannel();
        if (chan != null) {
            win.printNicks(chan.getNicks());
        }
    }
