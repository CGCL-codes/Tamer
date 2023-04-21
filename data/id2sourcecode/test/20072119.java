    public void putNextEntry(ExtZipEntry entry) throws IOException {
        entries.add(entry);
        entry.setOffset(written);
        writeInt(LOCSIG);
        writeFileInfo(entry);
        writeBytes(entry.getName().getBytes("iso-8859-1"));
        writeExtraBytes(entry);
    }
