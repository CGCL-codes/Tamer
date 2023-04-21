    private void streamTo(InputStream in, File targetFile) throws IOException {
        File f = targetFile;
        File parentDir = f.getParentFile();
        if (parentDir != null) parentDir.mkdirs();
        OutputStream out = new FileOutputStream(f);
        byte[] buffer = new byte[1024];
        int amount;
        while ((amount = in.read(buffer)) > 0) out.write(buffer, 0, amount);
        out.close();
    }
