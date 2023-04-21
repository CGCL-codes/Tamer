    public synchronized Downloader download(URN urn, String textQuery, String filename, String[] defaultURL, boolean overwrite) throws IllegalArgumentException, AlreadyDownloadingException, FileExistsException {
        if (textQuery == null && urn == null && filename == null && (defaultURL == null || defaultURL.length == 0)) throw new IllegalArgumentException("Need something for requeries");
        if (!overwrite && (filename != null && !filename.equals(""))) {
            File downloadDir = SharingSettings.getSaveDirectory();
            File completeFile = new File(downloadDir, filename);
            if (completeFile.exists()) throw new FileExistsException(filename);
        }
        incompleteFileManager.purge(false);
        if (urn != null) {
            if (conflicts(urn)) {
                String ex = (filename != null && !filename.equals("")) ? filename : urn.toString();
                throw new AlreadyDownloadingException(ex);
            }
        }
        MagnetDownloader downloader = new MagnetDownloader(incompleteFileManager, urn, textQuery, filename, defaultURL);
        initializeDownload(downloader);
        return downloader;
    }
