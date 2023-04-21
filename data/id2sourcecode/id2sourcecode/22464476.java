    public static Downloader download(RemoteFileDesc[] files, boolean overwrite, GUID queryGUID) throws FileExistsException, AlreadyDownloadingException, java.io.FileNotFoundException {
        return download(files, Collections.EMPTY_LIST, overwrite, queryGUID);
    }
