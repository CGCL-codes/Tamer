    public byte[] getStoppableResource(URL url, DownloadThread sourceThread) throws IOException {
        log.debug(".getResource(): " + url);
        HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
        long fileSize = httpConnection.getContentLength();
        InputStream in = httpConnection.getInputStream();
        byte[] buffer = new byte[4096];
        int bytes_read;
        int progressChunkSize = 100 / SystemRegistry.getReg().getListModel().size();
        int totalPercent;
        ByteArrayOutputStream bufferOut = new ByteArrayOutputStream();
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new GetSpeed(), 0, 1000);
        while ((bytes_read = in.read(buffer)) != -1 && !sourceThread.getStopRequest()) {
            bufferOut.write(buffer, 0, bytes_read);
            currentBytes += bytes_read;
            percentageDownloaded = (int) ((currentBytes * 100) / fileSize);
            SystemRegistry.getReg().getView().getMainPanel().getCurrentProgress().setValue(percentageDownloaded);
            totalPercent = (int) ((progressChunkSize * num) + (percentageDownloaded * 0.01 * progressChunkSize));
            if (totalPercent != 0) {
                SystemRegistry.getReg().getView().getMainPanel().getTotalProgress().setValue(totalPercent);
            }
        }
        timer.cancel();
        byte[] sresponse = bufferOut.toByteArray();
        httpConnection.disconnect();
        return sresponse;
    }
