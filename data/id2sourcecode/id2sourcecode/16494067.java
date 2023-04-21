    private void downloadHTTP(Download dl) {
        dl.setState(STATE_DOWNLOADING);
        fireDownloadStateChanged(dl, STATE_QUEUED, STATE_DOWNLOADING);
        OutputStream out = null;
        URLConnection conn = null;
        InputStream in = null;
        try {
            URL url = new URL(dl.getAddress());
            File file = new File(dl.getToFile());
            if (!file.exists()) {
                if (file.isDirectory()) {
                    file.mkdirs();
                } else {
                    file.createNewFile();
                }
            }
            out = new BufferedOutputStream(new FileOutputStream(file));
            conn = url.openConnection();
            double fileSize = conn.getContentLength();
            dl.setFileSize((fileSize * 1024) * 1024);
            in = conn.getInputStream();
            byte[] buffer = new byte[1024];
            int numRead;
            long numWritten = 0;
            while ((numRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, numRead);
                numWritten += numRead;
                dl.setProgress(numWritten);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (IOException ioe) {
            }
        }
        dl.setState(STATE_DONE);
        fireDownloadStateChanged(dl, STATE_DOWNLOADING, STATE_DONE);
        m_Queue.removeDownload(dl);
        removeDownload(dl);
    }
