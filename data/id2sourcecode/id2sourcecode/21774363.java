    private MyBrowserLauncher() {
        try {
            if (!Desktop.isDesktopSupported()) {
                httpSupported = false;
                return;
            }
            Desktop desktop = Desktop.getDesktop();
            if (desktop.isSupported(Desktop.Action.BROWSE)) {
                httpSupported = true;
            }
        } catch (Exception ex) {
            Logger.getLogger(MyBrowserLauncher.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
