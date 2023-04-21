    public void putNextEntry(ZipEntry e) throws IOException {
        if ((!useZip64) && this.entries.size() >= ZIP32_MAX_ENTRIES) {
            throw new IOException("Too many files in archive. Zip32 archive format does not allow to store more than " + ZIP32_MAX_ENTRIES + " files.");
        }
        ensureOpen();
        if (entry != null) {
            closeEntry();
        }
        if (e.time == -1) {
            e.setTime(System.currentTimeMillis());
        }
        if (e.getMethod() == -1) {
            e.setMethod(ZipEntry.DEFLATED);
        }
        e.flag = 8;
        if (charset.name().equals(CHARSET_UTF8)) {
            e.flag += 2048;
        }
        if (useZip64) {
            e.version = ZIP64VERSION;
        } else {
            e.version = ZIPVERSION;
        }
        e.offset = volumeStrategy == null ? totalWritten : ((VolumeOutputStream) out).getWrittenInCurrentVolume();
        e.volumeNumber = volumeStrategy == null ? 0 : volumeStrategy.getCurrentVolumeNumber();
        writeLOC(e);
        entry = e;
    }
