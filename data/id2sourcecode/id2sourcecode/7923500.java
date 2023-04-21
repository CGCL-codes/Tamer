    @Override
    public void deflate(String srcPath, String destAbsFilePath) throws IOException {
        log.info("************** Started Deflate() ******************");
        initDeflater(srcPath, destAbsFilePath);
        InputStream fis = new BufferedInputStream(new FileInputStream(srcFile));
        int readCount = 0;
        byte buffer[] = new byte[DATA_BUFFER_SIZE];
        log.info("Compressing data....");
        while ((readCount = fis.read(buffer)) != -1) {
            gzis.write(buffer, 0, readCount);
        }
        log.info("Finished compressing data....");
        gzis.close();
        log.info("************** Finished deflate() ******************");
    }
