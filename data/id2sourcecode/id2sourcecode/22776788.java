    public boolean downloadItem(String itemURL) {
        boolean success = false;
        try {
            byte[] chunk = new byte[1024];
            int bytesRead = 0;
            URL url = new URL(itemURL);
            InputStream inS = url.openStream();
            OutputStream[] outS = DownloadStreamFactory.createDownloadOutputStreams(config);
            startProgressDisplay();
            do {
                bytesRead = inS.read(chunk);
                for (int i = 0; (bytesRead > 0) && (i < outS.length); i++) {
                    outS[i].write(chunk, 0, bytesRead);
                }
                moveProgressDisplay();
            } while (bytesRead > 0);
            endProgressDisplay();
            for (int i = 0; i < outS.length; i++) {
                outS[i].flush();
                outS[i].close();
            }
            inS.close();
            success = true;
        } catch (Exception e) {
            error("Failed to download item : " + itemURL);
            error(e.getMessage());
        }
        return success;
    }
