    public void actionPerformed(ActionEvent e) {
        SandIRCFrame frame = SandIRCFrame.getInstance();
        if (frame.getCurrentWindowContainer() == null) {
            return;
        }
        IRCWindow win = frame.getCurrentWindowContainer().getSelectedWindow();
        if (win.getDocument().getChannel() != null) {
            boolean b = !win.isUserListShowing();
            if (b) {
                System.out.println("hide");
                putValue(Action.NAME, "Hide User List");
            } else {
                System.out.println("show");
                putValue(Action.NAME, "Show User List");
            }
            win.showUserList(b);
        }
    }
