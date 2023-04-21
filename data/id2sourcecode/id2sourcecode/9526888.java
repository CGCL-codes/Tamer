    private void sendFileData(String file) throws IOException, FileNotFoundException {
        InputStream fis = getClass().getResourceAsStream(file);
        if (fis == null) throw new FileNotFoundException(file);
        byte[] data = new byte[fis.available()];
        if (isHttp1) {
            pout.println("HTTP/1.0 200 Document follows");
            pout.println("Content-length: " + data.length);
            if (file.endsWith(".gif")) pout.println("Content-type: image/gif"); else if (file.endsWith(".html") || file.endsWith(".htm")) pout.println("Content-Type: text/html"); else pout.println("Content-Type: application/octet-stream");
            pout.println();
        }
        int bytesread = 0;
        do {
            bytesread = fis.read(data);
            if (bytesread > 0) pout.write(data, 0, bytesread);
        } while (bytesread != -1);
        pout.flush();
    }
