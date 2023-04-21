    private void formWindowDeactivated(java.awt.event.WindowEvent evt) {
        if (Settings.getInstance().readBool("gui.saveWindowPos")) Settings.getInstance().writeWindowPos("logwindow", getLocation());
    }
