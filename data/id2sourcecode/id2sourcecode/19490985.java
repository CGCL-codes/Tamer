    public static File download(URL url) throws IOException {
        logger.info("Downloading torrent file: " + url.toString());
        if (!url.getProtocol().equals("http")) throw new IOException("Unsupported protocol: " + url.getProtocol());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestProperty("User-Agent", "TorrentDemonio");
        conn.connect();
        int response = conn.getResponseCode();
        if (response != HttpURLConnection.HTTP_ACCEPTED && response != HttpURLConnection.HTTP_OK) {
            throw new IOException(conn.getResponseMessage());
        }
        String filename = conn.getHeaderField("Content-Disposition");
        if (filename != null) {
            int index1 = filename.indexOf("filename=") + 10;
            int index2 = filename.indexOf('"', index1);
            filename = index1 < 0 || index2 < 0 ? null : filename.substring(index1, index2);
        }
        if (filename != null && filename.length() == 0) {
            filename = url.getFile();
        }
        if (filename == null || filename.length() == 0 || filename.equals("/")) {
            filename = url.getHost();
        }
        byte[] buffer = new byte[1024];
        int bytesRead = 0;
        File save = new File(ConfigManager.getMetafilesDirectory() + File.separator + filename);
        InputStream in = conn.getInputStream();
        FileOutputStream out = new FileOutputStream(save);
        while ((bytesRead = in.read(buffer)) != -1) {
            out.write(buffer, 0, bytesRead);
        }
        out.flush();
        out.close();
        in.close();
        logger.info("Download OK. Filename: " + filename);
        return save;
    }
