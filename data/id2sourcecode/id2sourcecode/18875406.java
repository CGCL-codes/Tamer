    public void run() {
        System.out.println("downloading '" + url.toString() + "' to: " + dstFile.getAbsolutePath());
        DataOutputStream dos;
        try {
            dos = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(dstFile)));
            byte[] buffer = new byte[1024 * 4];
            DataInputStream is = new DataInputStream(url.openStream());
            long readed = 0;
            for (int i = is.read(buffer); !DownloadUtilities.canceled && i > 0; i = is.read(buffer)) {
                dos.write(buffer, 0, i);
                readed += i;
            }
            dos.close();
            is.close();
            is = null;
            dos = null;
            if (DownloadUtilities.canceled) {
                System.err.println("[RemoteClients] '" + url + "' CANCELED.");
                dstFile.delete();
                dstFile = null;
            } else {
                DownloadUtilities.addDownloadedURL(url, dstFile.getAbsolutePath());
            }
        } catch (Exception e) {
            DownloadUtilities.downloadException = e;
        }
    }
