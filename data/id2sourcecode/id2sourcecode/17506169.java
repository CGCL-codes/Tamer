    public void handleError(Exception e, MaverickString status) {
        try {
            getChannel(SCREEN_CHANNEL).PRINT(e, status);
        } catch (MaverickException mve) {
            mve.printStackTrace(System.err);
        }
    }
