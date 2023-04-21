    public static File downloadFile(RemoteBossaDataSource ds, String dir, FileDownloadMonitor monitor) throws IOException {
        File file = null;
        BufferedOutputStream out = null;
        BufferedInputStream in = null;
        try {
            URL url = ds.getUrl();
            monitor.fireEvent(new FileDownloadEvent("Connecting..."));
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestProperty("Host", ds.getHost());
            String fileName = parseFileName(http);
            if (fileName == null) {
                fileName = new File(url.getFile()).getName();
            }
            file = new File(dir, fileName);
            out = new BufferedOutputStream(new FileOutputStream(file));
            http.connect();
            in = new BufferedInputStream(http.getInputStream());
            byte[] buff = new byte[1024];
            int len;
            long size = 0;
            try {
                size = Long.parseLong(http.getHeaderField("Content-Length"));
            } catch (Exception e) {
                logger.log(Level.WARNING, "Can't parse size of file: " + url, e);
            }
            monitor.fireEvent(new FileDownloadEvent("Start dowloading file: " + fileName, Type.START, size));
            while ((len = in.read(buff)) != -1) {
                out.write(buff, 0, len);
                monitor.fireEvent(new FileDownloadEvent(len));
            }
            out.flush();
            monitor.fireEvent(new FileDownloadEvent("Finished"));
            return file;
        } finally {
            if (out != null) out.close();
            if (in != null) in.close();
        }
    }
