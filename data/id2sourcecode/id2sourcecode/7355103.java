    public boolean read(URL downloadUrl) {
        try {
            InputStreamReader ISR = new InputStreamReader(downloadUrl.openStream());
            if (!read(ISR)) {
                return false;
            }
            ISR.close();
        } catch (IOException e) {
            ConsoleDialog.writeError("An error occurred while trying to read " + "the torrent file", e);
            return false;
        }
        return true;
    }
