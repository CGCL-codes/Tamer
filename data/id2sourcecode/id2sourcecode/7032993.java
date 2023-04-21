    private void createFileFromStream(InputStream inputStream, File targetfile) throws IOException {
        FileOutputStream fos = new FileOutputStream(targetfile);
        int bytesread = 0;
        byte[] buf = new byte[1024];
        do {
            bytesread = inputStream.read(buf);
            if (bytesread > 0) {
                fos.write(buf, 0, bytesread);
            }
        } while (bytesread != -1);
        fos.close();
        inputStream.close();
    }
