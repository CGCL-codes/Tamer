    public void write(File dest, boolean forcePreferredSize) throws IOException {
        int bytesRead;
        if (getFrameCount() == 0) {
            throw new IOException("Tag doesn't contain any frames");
        }
        if (!dest.exists()) {
            OutputStream fout = new BufferedOutputStream(new FileOutputStream(dest));
            write(fout);
            fout.close();
            return;
        }
        if (!dest.canWrite()) {
            throw new IOException("Can't write on file " + dest);
        }
        byte[] headerData = new byte[10];
        File tempFile;
        boolean canOverwritePresentTag = false;
        boolean foundTag;
        int tagSize;
        InputStream fin = new PushbackInputStream(new BufferedInputStream(new FileInputStream(dest)), 10);
        bytesRead = fin.read(headerData, 0, 10);
        Bytes.checkRead(bytesRead, 10, "ID3v2Tag::write(File,boolean)");
        ((PushbackInputStream) fin).unread(headerData, 0, headerData.length);
        foundTag = isTag(headerData, 0);
        recalculateSizeIfNeccessary();
        logger.debug("ID3v2Tag::write(File,boolean): " + "Writing tag to file " + dest + "; forcePreferredSize = " + forcePreferredSize);
        if (foundTag) {
            tagSize = (int) Bytes.convertLong(headerData, 7, 6, 4) + 10;
            canOverwritePresentTag = (tagSize >= contentSize && !forcePreferredSize) || (tagSize == getSize() && forcePreferredSize);
            if (canOverwritePresentTag) {
                logger.info("ID3v2Tag::write(File,boolean):" + "Old tag will be overwritten");
                int oldPreferredSize = getPreferredSize();
                setPreferredSize(tagSize, ID3Constants.ABSOLUTE);
                fin.close();
                RandomAccessFile fout = new RandomAccessFile(dest, "rw");
                ByteArrayOutputStream bout = new ByteArrayOutputStream(getSize());
                write(bout);
                fout.write(bout.toByteArray());
                fout.close();
                setPreferredSize(oldPreferredSize, ID3Constants.ABSOLUTE);
                return;
            } else {
                logger.info("ID3v2Tag::write(File,boolean):" + "Old tag will be discarded");
                byte[] tagData = new byte[tagSize];
                bytesRead = fin.read(tagData, 0, tagData.length);
                Bytes.checkRead(bytesRead, tagData.length, "ID3v2Tag::write(File,boolean)");
                tagData = null;
            }
        }
        tempFile = File.createTempFile(dest.getName(), ".tmp", dest.getParentFile());
        copyData(fin, tempFile);
        fin.close();
        fin = new BufferedInputStream(new FileInputStream(tempFile));
        FileOutputStream fout = null;
        try {
            fout = new FileOutputStream(dest);
            write(fout);
            copyData(fin, fout);
        } catch (IOException e) {
            fin.close();
            fout.close();
            boolean couldDelete = dest.delete();
            boolean couldRename = tempFile.renameTo(dest);
            logger.error("ID3v2Tag::write(File,boolean): " + "couldDelete: " + couldDelete + "; couldRename: " + couldRename, e);
            throw e;
        }
        fin.close();
        fout.close();
        if (!tempFile.delete()) tempFile.deleteOnExit();
    }
