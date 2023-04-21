    public ZipArchive(String archiveFileName) throws IOException {
        this.archiveEntries = new HashMap();
        this.archiveFile = new File(archiveFileName);
        if (logger.isDebugEnabled()) {
            logger.debug("Loading archive file: " + archiveFile + ", " + new Date(archiveFile.lastModified()));
        }
        if (archiveFile.exists() && archiveFile.length() > 0) {
            ZipFile zipFile = new ZipFile(archiveFile);
            try {
                Enumeration zipEntries = zipFile.entries();
                while (zipEntries.hasMoreElements()) {
                    ZipEntry zipEntry = (ZipEntry) zipEntries.nextElement();
                    if (!zipEntry.isDirectory()) {
                        InputStream input = zipFile.getInputStream(zipEntry);
                        ByteArrayOutputStream output = new ByteArrayOutputStream();
                        byte[] buffer = new byte[2048];
                        int readBytes = input.read(buffer);
                        while (readBytes != -1) {
                            output.write(buffer, 0, readBytes);
                            readBytes = input.read(buffer);
                        }
                        ZipArchiveEntry archiveEntry = new ZipArchiveEntry(zipEntry.getName(), output.toByteArray(), zipEntry.getTime());
                        output.close();
                        input.close();
                        if (logger.isDebugEnabled()) {
                            logger.debug("Loading archive entry: " + new Date(archiveEntry.getTime()) + " " + archiveEntry.getName());
                        }
                        archiveEntries.put(archiveEntry.getName(), archiveEntry);
                    }
                }
            } finally {
                zipFile.close();
            }
        }
    }
