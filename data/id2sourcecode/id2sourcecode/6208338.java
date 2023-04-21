    @Override
    protected IStatus run(IProgressMonitor monitor) {
        if (!Controller.getInstance().getDownloadController().isItemDownloading(_audioItem)) {
            Controller.getInstance().getEventController().firePodcastsUpdateChange(EventConstants.EVT_PODCAST_DOWNLOAD_BEGIN, null, _audioItem);
            monitor.beginTask(Messages.getString("PodcastItemDownloaderJob.MainTask") + " " + _audioItem.getTitle(), 1);
            String temporaryFileName = _fileName + TEMPORARY_EXTENSION;
            try {
                Log.getInstance(PodcastItemDownloaderJob.class).debug("Start download of " + _sourceUrl + " to " + _fileName);
                monitor.subTask(Messages.getString("PodcastItemDownloaderJob.Connecting") + " " + _sourceUrl);
                HttpURLConnection connection = NetworkManager.getInstance().getConnection(_sourceUrl);
                int size = connection.getContentLength();
                monitor.beginTask(Messages.getString("PodcastItemDownloaderJob.MainTask") + " " + _audioItem.getTitle(), (size / DISPLAY_BUFFER_SIZE) + 4);
                monitor.worked(1);
                if (monitor.isCanceled()) {
                    Controller.getInstance().getEventController().firePodcastsUpdateChange(EventConstants.EVT_PODCAST_DOWNLOAD_END, new Boolean(true), _audioItem);
                    return Status.CANCEL_STATUS;
                }
                monitor.subTask(Messages.getString("PodcastItemDownloaderJob.CreateFile") + " " + temporaryFileName);
                bis = new BufferedInputStream(connection.getInputStream());
                bos = new BufferedOutputStream(new FileOutputStream(temporaryFileName));
                monitor.worked(1);
                if (monitor.isCanceled()) {
                    closeDownload();
                    deleteFile(temporaryFileName);
                    Controller.getInstance().getEventController().firePodcastsUpdateChange(EventConstants.EVT_PODCAST_DOWNLOAD_END, new Boolean(true), _audioItem);
                    return Status.CANCEL_STATUS;
                }
                monitor.subTask(Messages.getString("PodcastItemDownloaderJob.Downloading") + " " + _sourceUrl);
                boolean downLoading = true;
                byte[] buffer;
                int downloaded = 0;
                int read;
                int stepRead = 0;
                while (downLoading) {
                    if (size - downloaded > MAX_BUFFER_SIZE) {
                        buffer = new byte[MAX_BUFFER_SIZE];
                    } else {
                        buffer = new byte[size - downloaded];
                    }
                    read = bis.read(buffer);
                    if (read > 0) {
                        bos.write(buffer, 0, read);
                        downloaded += read;
                        stepRead += read;
                    } else {
                        downLoading = false;
                    }
                    if (monitor.isCanceled()) {
                        closeDownload();
                        deleteFile(temporaryFileName);
                        Controller.getInstance().getEventController().firePodcastsUpdateChange(EventConstants.EVT_PODCAST_DOWNLOAD_END, new Boolean(true), _audioItem);
                        return Status.CANCEL_STATUS;
                    }
                    if (stepRead >= DISPLAY_BUFFER_SIZE) {
                        monitor.worked(1);
                        stepRead = 0;
                    }
                }
                monitor.worked(1);
            } catch (MalformedURLException e) {
                processError("Error while binary downloading: MalformedURLException.");
            } catch (IOException e) {
                processError("Error while binary downloading: IOException: " + e.getMessage() + ".");
            } finally {
                monitor.subTask(Messages.getString("PodcastItemDownloaderJob.ClosingDownload"));
                closeDownload();
                monitor.worked(1);
            }
            finalizeDownload(temporaryFileName);
            if (_playAfterDownload) {
                Controller.getInstance().getPlaylistController().playFile(_playlist, _audioItem);
            }
            Log.getInstance(PodcastItemDownloaderJob.class).debug("End of download.");
            Controller.getInstance().getEventController().firePodcastsUpdateChange(EventConstants.EVT_PODCAST_DOWNLOAD_END, new Boolean(false), _audioItem);
        } else {
            Log.getInstance(PodcastItemDownloaderJob.class).debug("Item already being downloaded: " + _audioItem.getTitle());
        }
        return Status.OK_STATUS;
    }
