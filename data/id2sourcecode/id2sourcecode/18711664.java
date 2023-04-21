    private boolean isSteamOpened() {
        if (stream != null && stream instanceof FileInputStream) {
            return ((FileInputStream) stream).getChannel().isOpen();
        } else {
            return stream != null;
        }
    }
