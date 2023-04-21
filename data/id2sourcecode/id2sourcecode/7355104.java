    public boolean read(File file) {
        try {
            InputStreamReader ISR = new InputStreamReader(new FileInputStream(file));
            if (!read(ISR)) {
                return false;
            }
            ISR.close();
        } catch (Exception e) {
            ConsoleDialog.writeError("An error occurred while trying to read " + "the torrent file", e);
            return false;
        }
        return true;
    }
