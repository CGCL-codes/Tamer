    private void downloadFile(URL remoteFile, File localFile) throws FileNotFoundException, IOException {
        URLConnection remoteConnection = remoteFile.openConnection();
        BufferedInputStream in = new BufferedInputStream(remoteConnection.getInputStream());
        BufferedOutputStream out = new BufferedOutputStream(new DataOutputStream(new FileOutputStream(localFile)));
        String filename = remoteFile.toString();
        FileSize fileSize = new FileSize(remoteConnection.getContentLength());
        FileSize readSize;
        int readBytes = 0;
        int readByte;
        while ((readByte = in.read()) != -1) {
            out.write(readByte);
            readBytes++;
            if ((readBytes % 1024) == 0) {
                readSize = new FileSize(readBytes);
                String status = "Downloading " + filename + ": " + readSize.getReadable() + " of " + fileSize.getReadable();
                ProgressUpdateMessage message = new ProgressUpdateMessage(status);
                this.setChanged();
                this.notifyObservers(message);
            }
        }
        out.close();
        in.close();
    }
