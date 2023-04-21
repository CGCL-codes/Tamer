    private Image getImage() {
        try {
            URL url = (URL) Activator.getDefault().getBundle().findEntries("icons", "dtu.gif", false).nextElement();
            return new Image(Display.getDefault(), url.openStream());
        } catch (IOException ex) {
            DTULogger.error(ex.getMessage(), ex);
        }
        return null;
    }
