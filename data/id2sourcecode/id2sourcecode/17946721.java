    private boolean downloadFile(URL url, File tempFile, int progressPortion) throws FileNotFoundException, IOException {
        FileOutputStream fos = new FileOutputStream(tempFile);
        out.println("Created temp file: " + tempFile);
        out.flush();
        HttpURLConnection uconn = (HttpURLConnection) url.openConnection();
        out.println("Getting data from " + url);
        out.flush();
        InputStream raw = uconn.getInputStream();
        InputStream in = new BufferedInputStream(raw);
        int contentLength = uconn.getContentLength();
        byte[] data = new byte[contentLength];
        int read = 0;
        int offs = 0;
        while (offs < contentLength && !abort) {
            read = in.read(data, offs, data.length - offs);
            if (read < 0) {
                break;
            }
            offs += read;
            progress = (int) (progressPortion * (offs / (double) contentLength));
        }
        in.close();
        if (offs != contentLength) {
            out.println("Error downloading " + url);
            out.flush();
            return false;
        }
        out.print("Completed downloading " + url + ", writing...");
        out.flush();
        fos.write(data);
        fos.close();
        out.println("Done!");
        out.flush();
        return true;
    }
