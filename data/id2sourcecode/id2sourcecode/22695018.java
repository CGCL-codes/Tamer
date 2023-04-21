    public static void downloadFile(URL url, File file, ProgressListener listener) throws IOException {
        int length = url.openConnection().getContentLength();
        int totalRead = 0;
        InputStream in = url.openStream();
        file.createNewFile();
        FileOutputStream out = new FileOutputStream(file);
        int read;
        byte[] buffer = new byte[4096];
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
            totalRead += read;
            if (listener != null) listener.progressed(new ProgressEvent(listener, ((float) totalRead) / ((float) length)));
        }
        out.close();
        in.close();
    }
