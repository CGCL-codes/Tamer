    private void readStreamedOutcome() throws FileNotFoundException, IOException {
        PacketisedInputStream pis = new PacketisedInputStream(socket.getInputStream());
        ZipInputStream zis = new ZipInputStream(pis);
        int avail;
        int count = 0;
        logger.fine("Reading streamed outcome.");
        if ((avail = zis.available()) != 0) {
            if (directOutput == null) {
                FileOutputStream fos = null;
                File streamedDir = null;
                ZipEntry entry = zis.getNextEntry();
                byte[] buffer = new byte[4096];
                int read = 0;
                OutcomeManager.clearCache();
                while (entry != null) {
                    String zipName = entry.getName();
                    int lastSep = zipName.lastIndexOf("/");
                    String filename = zipName.substring(lastSep + 1);
                    String dir = sessionDir + File.separator + zipName.substring(0, lastSep);
                    logger.fine("Receiving streamed file: " + zipName);
                    OutcomeManager.addStreamedFile(zipName);
                    streamedDir = new File(dir);
                    streamedDir.deleteOnExit();
                    File streamedFile = new File(dir, filename);
                    streamedFile.deleteOnExit();
                    streamedDir.mkdirs();
                    fos = new FileOutputStream(streamedFile);
                    while ((read = zis.read(buffer)) > 0) {
                        fos.write(buffer, 0, read);
                    }
                    fos.close();
                    zis.closeEntry();
                    File parent = streamedDir;
                    while (parent.getParentFile() != null) {
                        parent.deleteOnExit();
                        parent = parent.getParentFile();
                    }
                    parent = parent.getParentFile();
                    entry = zis.getNextEntry();
                }
            } else {
                logger.fine("Direct output files into destination directory.");
                copyFile(zis);
            }
            long skipped = pis.skip(Long.MAX_VALUE);
        }
    }
