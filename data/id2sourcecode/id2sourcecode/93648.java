    public void putNextEntry(ZipEntry entry) throws IOException {
        if (entries == null) throw new ZipException("ZipOutputStream was finished");
        int method = entry.getMethod();
        int flags = 0;
        if (method == -1) method = defaultMethod;
        if (method == STORED) {
            if (entry.getCompressedSize() >= 0) {
                if (entry.getSize() < 0) entry.setSize(entry.getCompressedSize()); else if (entry.getSize() != entry.getCompressedSize()) throw new ZipException("Method STORED, but compressed size != size");
            } else entry.setCompressedSize(entry.getSize());
            if (entry.getSize() < 0) throw new ZipException("Method STORED, but size not set");
            if (entry.getCrc() < 0) throw new ZipException("Method STORED, but crc not set");
        } else if (method == DEFLATED) {
            if (entry.getCompressedSize() < 0 || entry.getSize() < 0 || entry.getCrc() < 0) flags |= 8;
        }
        if (curEntry != null) closeEntry();
        if (entry.getTime() < 0) entry.setTime(System.currentTimeMillis());
        entry.flags = flags;
        entry.offset = offset;
        entry.setMethod(method);
        curMethod = method;
        writeLeInt(LOCSIG);
        writeLeShort(method == STORED ? ZIP_STORED_VERSION : ZIP_DEFLATED_VERSION);
        writeLeShort(flags);
        writeLeShort(method);
        writeLeInt(entry.getDOSTime());
        if ((flags & 8) == 0) {
            writeLeInt((int) entry.getCrc());
            writeLeInt((int) entry.getCompressedSize());
            writeLeInt((int) entry.getSize());
        } else {
            writeLeInt(0);
            writeLeInt(0);
            writeLeInt(0);
        }
        byte[] name = entry.getName().getBytes();
        if (name.length > 0xffff) throw new ZipException("Name too long.");
        byte[] extra = entry.getExtra();
        if (extra == null) extra = new byte[0];
        writeLeShort(name.length);
        writeLeShort(extra.length);
        out.write(name);
        out.write(extra);
        offset += LOCHDR + name.length + extra.length;
        curEntry = entry;
        crc.reset();
        if (method == DEFLATED) def.reset();
        size = 0;
    }
