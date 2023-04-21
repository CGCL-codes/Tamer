    public void downloadDirect(String url, String localFile) throws MalformedURLException, FileNotFoundException, IOException {
        FileOutputStream toFile = new FileOutputStream(localFile);
        InputStream fromServer = (new URL(url)).openConnection().getInputStream();
        try {
            byte[] buffer = new byte[4096];
            int bytesRead = 0;
            while ((bytesRead = fromServer.read(buffer)) != -1) {
                toFile.write(buffer, 0, bytesRead);
            }
        } finally {
            fromServer.close();
            toFile.close();
        }
    }
