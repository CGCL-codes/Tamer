    public static void downloadFile(String source, File target) throws IOException {
        URL url = new URL(source);
        URLConnection urlc = url.openConnection();
        InputStream raw = urlc.getInputStream();
        int contentLength = urlc.getContentLength();
        if (contentLength == -1) {
            throw new IOException("Unknown file length (content-length header field)");
        }
        InputStream in = new BufferedInputStream(raw);
        byte[] data = new byte[contentLength];
        int bytesRead = 0;
        int offset = 0;
        while (offset < contentLength) {
            bytesRead = in.read(data, offset, data.length - offset);
            if (bytesRead == -1) break;
            offset += bytesRead;
        }
        in.close();
        if (offset != contentLength) {
            throw new IOException("Only read " + offset + " bytes, but expected " + contentLength + " bytes");
        }
        FileOutputStream out = new FileOutputStream(target);
        out.write(data);
        out.flush();
        out.close();
    }
