    public boolean checkBroswer() {
        if (desktop.isDesktopSupported() && desktop.isSupported(Desktop.Action.BROWSE)) {
            return true;
        } else {
            return false;
        }
    }
