    private void writeDirectory(ZipOutputStream zip, Filelike entry, ULog log) throws ValidationException, IOException {
        String archiveName = getNameStrategy().getArchiveName(log, entry);
        if (archiveName != null && archiveName.length() > 0) {
            if (!archiveName.endsWith("/")) {
                archiveName += "/";
            }
            ZipEntry ze = new ZipEntry(archiveName);
            log.fine("writing entry {0} (directory)", new Quote(ze.getName()));
            ze.setSize(0);
            ze.setMethod(ZipEntry.STORED);
            ze.setCompressedSize(0);
            ze.setCrc(0);
            zip.putNextEntry(ze);
            zip.closeEntry();
        }
    }
