    public void saveTorrent() throws java.io.FileNotFoundException, java.io.IOException {
        URLConnection connection;
        connection = downloadUrl.openConnection();
        connection.setRequestProperty("User-Agent", Driver.agent);
        connection.connect();
        InputStream ourStream = connection.getInputStream();
        BufferedInputStream ourBuffer = new BufferedInputStream(ourStream);
        String saveTo = Buttress.getButtress().getDriver().getDownloadFolder() + RunTorrent.FILE_SEP;
        FileOutputStream ourOut = new FileOutputStream(saveTo + torrentName);
        savedFileSys = saveTo + torrentName;
        int size = ourStream.available();
        if (size == 0) {
            throw new IOException("Cannot read the source file - 0 bytes " + "available to be read.");
        }
        byte[] temp = new byte[size];
        int readIn;
        while ((readIn = ourBuffer.read(temp, 0, size)) != -1) {
            ourOut.write(temp, 0, readIn);
        }
        ourOut.close();
    }
