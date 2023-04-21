    private boolean downloadFile(DownloadJob job) throws IOException {
        DownloadEntity downloadEntity = job.getDownloadEntity();
        job.notifyDownloadStarted();
        URL url = new URL(downloadEntity.path);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setConnectTimeout(30000);
        httpURLConnection.setReadTimeout(30000);
        httpURLConnection.setRequestMethod("GET");
        httpURLConnection.setDoOutput(true);
        httpURLConnection.connect();
        job.setTotalSize(httpURLConnection.getContentLength());
        String path = job.getDestination() + File.separator + downloadEntity.title + DOUT + Utilities.getDownloadFileFormat(downloadEntity.title);
        try {
            boolean success = (new File(path)).mkdirs();
            if (success) {
                Log.i(Constants.TAG, "Directory: " + path + " created");
            }
        } catch (Exception e) {
            Log.e(Constants.TAG, "Error creating folder", e);
            return false;
        }
        FileOutputStream fileOutputStream = new FileOutputStream(new File(path, downloadEntity.title));
        InputStream in = httpURLConnection.getInputStream();
        byte[] buffer = new byte[1024];
        int lenght = 0;
        while ((lenght = in.read(buffer)) > 0) {
            fileOutputStream.write(buffer, 0, lenght);
            job.setDownloadedSize(job.getDownloadedSize() + lenght);
        }
        if (job.getDownloadedSize() == job.getTotalSize()) {
            job.notifyDownloadCompleted();
        } else {
            job.notifyDownloadFatal();
        }
        fileOutputStream.close();
        return false;
    }
