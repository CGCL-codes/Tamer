    protected int writeImageToFile(ComixPage page, FileOutputStream outputStream, IProgressMonitor monitor) throws IOException {
        URLConnection connection = page.getImageUrl().openConnection();
        int contentLength = connection.getContentLength();
        monitor.start(contentLength);
        byte[] byteBuffer = new byte[BUFFER_SIZE];
        InputStream inputStream = connection.getInputStream();
        BufferedInputStream bis = new BufferedInputStream(inputStream);
        BufferedOutputStream bos = new BufferedOutputStream(outputStream);
        int read;
        while ((read = bis.read(byteBuffer)) != -1) {
            bos.write(byteBuffer, 0, read);
            monitor.worked(read, null);
        }
        bos.close();
        bis.close();
        inputStream.close();
        monitor.done(null);
        return contentLength;
    }
