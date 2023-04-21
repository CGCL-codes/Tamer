    private Desktop getDesktop() {
        if (Desktop.isDesktopSupported() == false) return null;
        Desktop d = Desktop.getDesktop();
        if (d != null && d.isSupported(Action.OPEN)) return d; else return null;
    }
