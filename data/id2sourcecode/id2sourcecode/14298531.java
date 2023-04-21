    public static void extractArchive(File sourceFile, File destDir) throws IOException {
        destDir.mkdirs();
        ZipInputStream zis = null;
        try {
            zis = new ZipInputStream(new BufferedInputStream(new FileInputStream(sourceFile)));
            ReadableByteChannel zipEntryUnpacking = Channels.newChannel(zis);
            ZipEntry entry = zis.getNextEntry();
            while (entry != null) {
                FileOutputStream destFileStream = null;
                String currentEntry = entry.getName();
                File destFile = new File(destDir, currentEntry);
                File destinationParent = destFile.getParentFile();
                destinationParent.mkdirs();
                try {
                    if (!entry.isDirectory()) {
                        destFileStream = new FileOutputStream(destFile);
                        destFileStream.getChannel().transferFrom(zipEntryUnpacking, 0, 1 << 24);
                    }
                } catch (IOException ioe) {
                    Helper.logger.log(Level.SEVERE, "Ошибка при распаковке " + entry.getName(), ioe);
                } finally {
                    if (destFileStream != null) {
                        try {
                            destFileStream.close();
                        } catch (Exception e) {
                            Helper.logger.log(Level.SEVERE, "Ошибка при закрытии потока", e);
                        }
                    }
                }
                entry = zis.getNextEntry();
            }
        } finally {
            if (zis != null) {
                try {
                    zis.close();
                } catch (Exception e) {
                    Helper.logger.log(Level.SEVERE, "Ошибка при закрытии потока", e);
                }
            }
        }
    }
