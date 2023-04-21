    protected void writeFile(boolean isDirectory, InputStream inputFile, String filename, boolean verbose) throws IOException {
        if (writtenItems.contains(filename)) {
            if (verbose) {
                String msg = MessageFormat.format(Messages.getString("Creator.Ignoring"), new Object[] { filename });
                System.err.println(msg);
            }
            return;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CRC32 crc = new CRC32();
        long size;
        if (isDirectory) {
            size = 0;
        } else {
            size = copyFile(crc, inputFile, out);
        }
        ZipEntry entry = new ZipEntry(filename);
        entry.setCrc(crc.getValue());
        entry.setSize(size);
        outputStream.putNextEntry(entry);
        out.writeTo(outputStream);
        outputStream.closeEntry();
        writtenItems.add(filename);
        if (verbose) {
            long csize = entry.getCompressedSize();
            long perc;
            if (size == 0) perc = 0; else perc = 100 - (100 * csize) / size;
            String msg = MessageFormat.format(Messages.getString("Creator.Adding"), new Object[] { filename, Long.valueOf(size), Long.valueOf(entry.getSize()), Long.valueOf(perc) });
            System.err.println(msg);
        }
    }
