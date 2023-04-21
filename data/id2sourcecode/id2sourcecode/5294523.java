    private OutputStream getOutputStream(File file, boolean cached, OutputStreamListener listener) throws IOException {
        ZipOutputStream zout;
        if (compression.isMultiVolumes()) {
            File target = new File(encode(predecessor.getParentFile(file)), predecessor.getName(file));
            ZipVolumeStrategy strategy = new ZipVolumeStrategy(target, predecessor, cached, compression.getNbDigits());
            strategy.setListener(listener);
            zout = new ZipOutputStream(strategy, compression.getVolumeSize() * 1024 * 1024, compression.isUseZip64());
        } else {
            OutputStream base;
            if (cached) {
                base = predecessor.getCachedFileOutputStream(encode(file));
            } else {
                base = predecessor.getFileOutputStream(encode(file), false, listener);
            }
            zout = new ZipOutputStream(base, compression.isUseZip64());
        }
        if (compression.getLevel() >= 0) {
            zout.setLevel(compression.getLevel());
        } else {
            zout.setLevel(9);
        }
        if (compression.getCharset() != null) {
            zout.setCharset(compression.getCharset());
        }
        if (compression.getComment() != null) {
            zout.setComment(compression.getComment());
        }
        try {
            zout.putNextEntry(new ZipEntry(file.getName()));
        } catch (IOException e) {
            try {
                zout.close();
            } catch (IOException ignored) {
            }
            throw e;
        }
        return zout;
    }
