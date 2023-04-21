    private String writeRawBytes(byte[] file, String host, String path) throws IOException {
        Date now = new Date();
        long time = now.getTime();
        String filename = host.trim() + "_" + time + ".jpg";
        String imgname = path + "/images/complaintimg/" + filename;
        final FileOutputStream fos = new FileOutputStream(imgname);
        FileChannel fc = fos.getChannel();
        ByteBuffer buffer = ByteBuffer.wrap(file);
        fc.write(buffer);
        fc.close();
        return filename;
    }
