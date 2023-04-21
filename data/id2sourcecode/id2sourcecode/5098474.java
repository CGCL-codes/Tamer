    @Override
    public void inflate(String archiveFilePath, String outputDir) throws IOException {
        log.info("inflate >>>");
        initInflater(archiveFilePath, outputDir);
        OutputStream out = new BufferedOutputStream(new FileOutputStream(outputFile));
        log.info("Extracting to " + outputFile.getAbsolutePath());
        int readCount = 0;
        byte buffer[] = new byte[DATA_BUFFER_SIZE];
        log.info("Decompressing GZIP archive....");
        boolean isCorrupt = true;
        while ((readCount = gzipIn.read(buffer)) != -1) {
            isCorrupt = false;
            out.write(buffer, 0, readCount);
        }
        if (isCorrupt) {
            log.severe("Input file is corrupted or may not be in GZIP format");
        }
        out.flush();
        log.info("Finished decompressing GZIP archive....");
        out.close();
        gzipIn.close();
        if (isCorrupt) {
            log.severe("Input file is corrupted or may not be in GZIP format");
            throw new IOException("Input file is corrupted or may not be in GZIP format");
        }
        log.info("<<< inflate");
    }
