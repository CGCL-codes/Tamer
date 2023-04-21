    @Override
    public JobStatus run() {
        InputStream inputStream = null;
        FileOutputStream fileOutputStream = null;
        try {
            JsonGetSong song = JGroovex.getSongURL(correspondence.getSongID());
            inputStream = JGroovex.getSongStream(song.result.ip, song.result.streamKey);
            final CountingInputStream countingInputStream = new CountingInputStream(inputStream);
            fileOutputStream = new FileOutputStream(path.toFile());
            new Thread() {

                private int sleepTime = 100;

                @Override
                public void run() {
                    int readPrevious = 0;
                    int readNow = 0;
                    double startTime = System.currentTimeMillis();
                    terminateThread = false;
                    do {
                        try {
                            sleep(sleepTime);
                        } catch (InterruptedException e) {
                            GroofyLogger.getInstance().logError(e.getLocalizedMessage());
                        }
                        readNow = countingInputStream.getCount();
                        worked(readNow - readPrevious);
                        setDescription(String.format(Messages.getString("DownloadCorrespondenceToPathJob.DownloadedXToFile"), readNow / 1048576f, (readNow / ((float) (System.currentTimeMillis() - startTime) / 1000f)) / 1024f, path.toOSString()));
                        readPrevious = readNow;
                        if (isCancelled()) {
                            return;
                        }
                    } while (!terminateThread);
                }
            }.start();
            byte buffer[] = new byte[1024];
            int read;
            while ((read = countingInputStream.read(buffer)) > 0) {
                if (isCancelled()) {
                    return JobStatus.CANCELLED;
                }
                fileOutputStream.write(buffer, 0, read);
            }
            JGroovex.markSongComplete(song.result.streamServerID, song.result.streamKey, correspondence.getSongID());
        } catch (IOException e) {
            GroofyLogger.getInstance().logException(e);
            return JobStatus.ERROR;
        } catch (JsonParseException e) {
            GroofyLogger.getInstance().logError(String.format(Messages.getString("DownloadCorrespondenceToPathJob.UnableDownloadSong"), correspondence.getSongName(), correspondence.getArtistName()));
            return JobStatus.ERROR;
        } finally {
            IOUtils.closeQuietly(fileOutputStream);
            IOUtils.closeQuietly(inputStream);
            terminateThread = true;
        }
        return JobStatus.OK;
    }
