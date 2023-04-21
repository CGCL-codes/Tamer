    public static synchronized Downloader download(URN urn, String textQuery, String filename, String[] defaultURL, boolean overwrite) throws IllegalArgumentException, AlreadyDownloadingException, FileExistsException {
        return RouterService.download(urn, textQuery, filename, defaultURL, overwrite);
    }
