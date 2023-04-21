    public static void copyFile(InputStream is, String fileOutputPath) throws Exception {
        ByteArrayOutputStream bo = new ByteArrayOutputStream();
        byte b[] = new byte[1024];
        int c = 0;
        while ((c = is.read(b)) > 0) bo.write(b, 0, c);
        bo.flush();
        File f = new File(fileOutputPath);
        FileOutputStream fos = new FileOutputStream(f);
        fos.write(bo.toByteArray());
    }
