    private static void rsAddInZip(ZipOutputStream zos, File baseDir, File entryDir) {
        try {
            for (File entry : entryDir.listFiles()) {
                System.out.println(entry.getAbsolutePath());
                if (entry.isFile()) {
                    ZipEntry zipEntry = new ZipEntry(FileCommander.getRelativePath(baseDir, entry));
                    zipEntry.setComment("packed by me");
                    zos.putNextEntry(zipEntry);
                    Streamer.bufferedStreamCopy_noCloseOut(new FileInputStream(entry), zos);
                    zos.closeEntry();
                }
                rsAddInZip(zos, baseDir, entry);
            }
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
        }
    }
