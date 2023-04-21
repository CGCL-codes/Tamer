    private void dumpFile(HttpEntity entity, File partialDestinationFile, File destinationFile) throws IOException, NotEnoughSpaceException {
        if (showDebugOutput) Log.d(TAG, "DumpFile Called");
        if (!prepareForDownloadCancel) {
            mcontentLength = (int) entity.getContentLength();
            if (mcontentLength <= 0) {
                if (showDebugOutput) Log.d(TAG, "unable to determine the update file size, Set ContentLength to 1024");
                mcontentLength = 1024;
            } else if (showDebugOutput) Log.d(TAG, "Update size: " + (mcontentLength / 1024) + "KB");
            if (!SysUtils.EnoughSpaceOnSdCard(mcontentLength)) throw new NotEnoughSpaceException(res.getString(R.string.download_not_enough_space));
            mStartTime = System.currentTimeMillis();
            byte[] buff = new byte[64 * 1024];
            int read;
            RandomAccessFile out = new RandomAccessFile(partialDestinationFile, "rw");
            out.seek(localFileSize);
            InputStream is = entity.getContent();
            TimerTask progressUpdateTimerTask = new TimerTask() {

                @Override
                public void run() {
                    onProgressUpdate();
                }
            };
            Timer progressUpdateTimer = new Timer();
            try {
                mtotalDownloaded = localFileSize;
                progressUpdateTimer.scheduleAtFixedRate(progressUpdateTimerTask, 100, prefs.getProgressUpdateFreq());
                while ((read = is.read(buff)) > 0 && !prepareForDownloadCancel) {
                    out.write(buff, 0, read);
                    mtotalDownloaded += read;
                }
                out.close();
                is.close();
                if (!prepareForDownloadCancel) {
                    partialDestinationFile.renameTo(destinationFile);
                    if (showDebugOutput) Log.d(TAG, "Download finished");
                } else {
                    if (showDebugOutput) Log.d(TAG, "Download cancelled");
                }
            } catch (IOException e) {
                out.close();
                try {
                    destinationFile.delete();
                } catch (SecurityException ex) {
                    Log.e(TAG, "Unable to delete downloaded File. Continue anyway.", ex);
                }
            } finally {
                progressUpdateTimer.cancel();
                buff = null;
            }
        } else if (showDebugOutput) Log.d(TAG, "Download Cancel in Progress. Don't start Downloading");
    }
