    @Override
    protected void run(InputStream inputStream, OutputStream outputStream) throws Exception {
        ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream);
        zipOutputStream.putNextEntry(new ZipEntry(entryName));
        zipOutputStream.setLevel(level);
        transfer(inputStream, zipOutputStream);
        zipOutputStream.closeEntry();
        zipOutputStream.finish();
    }
