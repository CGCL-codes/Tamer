    private void openConnection() {
        try {
            connection = url.openConnection();
        } catch (IOException e) {
            logger.error("Error opening url", e);
        }
    }
