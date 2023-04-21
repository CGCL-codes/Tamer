    protected void doMain() {
        boolean owrite = hasOption('o');
        ExtractorThread imageInfo = new ExtractorThread(status, getAlbum(), owrite);
        imageInfo.start();
    }
