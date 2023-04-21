    public static int copy(InputStream in, OutputStream out, byte[] workbuff) throws IOException {
        int bytescopied = 0;
        int bytesread = 0;
        while ((bytesread = in.read(workbuff)) != -1) {
            out.write(workbuff, 0, bytesread);
            bytescopied += bytesread;
        }
        return bytescopied;
    }
