    @Override
    public void save(InputStream stream, String fileName, String path, String type) throws IOException {
        String rootPath = "";
        if (type.equals("file")) rootPath = fileRootPath;
        if (type.equals("image")) rootPath = imageRootPath;
        File dir = new File(rootPath + path);
        dir.mkdirs();
        FileOutputStream fs = new FileOutputStream(rootPath + path + fileName);
        byte[] buffer = new byte[1024 * 1024];
        int bytesum = 0;
        int byteread = 0;
        while ((byteread = stream.read(buffer)) != -1) {
            bytesum += byteread;
            fs.write(buffer, 0, byteread);
            fs.flush();
        }
        fs.close();
        stream.close();
    }
