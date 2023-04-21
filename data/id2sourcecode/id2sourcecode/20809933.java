    public synchronized Downloader download(RemoteFileDesc[] files, List alts, boolean overwrite, GUID queryGUID) throws FileExistsException, AlreadyDownloadingException, java.io.FileNotFoundException {
        String conflict = conflicts(files, null);
        if (conflict != null) throw new AlreadyDownloadingException(conflict);
        if (!overwrite) {
            File downloadDir = SharingSettings.getSaveDirectory();
            String filename = files[0].getFileName();
            File completeFile = new File(downloadDir, filename);
            if (completeFile.exists()) throw new FileExistsException(filename);
        }
        incompleteFileManager.purge(false);
        ManagedDownloader downloader = new ManagedDownloader(files, incompleteFileManager, queryGUID);
        initializeDownload(downloader);
        for (Iterator iter = alts.iterator(); iter.hasNext(); ) {
            RemoteFileDesc rfd = (RemoteFileDesc) iter.next();
            downloader.addDownload(rfd, false);
        }
        return downloader;
    }
