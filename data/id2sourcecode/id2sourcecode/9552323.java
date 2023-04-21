    public static Downloader download(RemoteFileDesc[] files, List alts, boolean overwrite, GUID queryGUID) throws FileExistsException, AlreadyDownloadingException, java.io.FileNotFoundException {
        return downloader.download(files, alts, overwrite, queryGUID);
    }
