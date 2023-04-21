    private final void copyInputStream(InputStream in, String zipEntryName) throws IOException {
        byte[] buffer = new byte[1024];
        int len;
        File dirs = new File(destinationDirectory);
        File f = new File(dirs, zipEntryName);
        makeDirs(f.getAbsolutePath(), zipEntryName);
        OutputStream out = new BufferedOutputStream(new FileOutputStream(f));
        while ((len = in.read(buffer)) >= 0) out.write(buffer, 0, len);
        out.close();
    }
