    public static Downloader download(RemoteFileDesc[] files, java.util.List alts, boolean overwrite, GUID queryGUID) throws FileExistsException, AlreadyDownloadingException, java.io.FileNotFoundException {
        return RouterService.download(files, alts, overwrite, queryGUID);
    }
