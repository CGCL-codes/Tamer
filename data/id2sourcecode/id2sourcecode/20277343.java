    @Override
    protected DownloadBean doInBackground() throws Exception {
        download.setStatus(DownloadStatus.DOWNLOADING);
        firePropertyChange("status", null, download);
        buildDirectoryChain(download.getSaveTo());
        File saveToFile = new File(download.getSaveTo() + File.separator + download.getSaveAs());
        if (saveToFile.exists()) {
            download.setStatus(DownloadStatus.COMPLETED);
        } else {
            try {
                URL url = new URL(download.getUrl());
                HttpURLConnection downFile = (HttpURLConnection) url.openConnection();
                byte[] buffer = new byte[downFile.getInputStream().available()];
                FileOutputStream fos = new FileOutputStream(saveToFile);
                while (downFile.getInputStream().read(buffer) != -1) {
                    fos.write(buffer);
                    buffer = new byte[downFile.getInputStream().available()];
                }
                fos.close();
                downFile.getInputStream().close();
                download.setStatus(DownloadStatus.COMPLETED);
                firePropertyChange("status", null, download);
            } catch (Exception e) {
                download.setStatus(DownloadStatus.ERROR);
                firePropertyChange("status", null, download);
            }
        }
        return download;
    }
