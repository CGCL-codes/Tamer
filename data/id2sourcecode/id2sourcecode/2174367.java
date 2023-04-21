    public boolean isHome() {
        if (!isHomeRestriction) return true;
        if ((this.gui == null) || (this.logonChannel == null)) return false; else {
            return gui.getUserDisplay().getChannel().toLowerCase().equals(logonChannel.toLowerCase());
        }
    }
