    protected void dumpImg(OutputStream out, SeekableLittleEndianAccessor slea) throws IOException {
        DataOutputStream os = new DataOutputStream(out);
        long oldPos = slea.getPosition();
        slea.seek(file.getOffset());
        for (int x = 0; x < file.getSize(); x++) {
            os.write(slea.readByte());
        }
        slea.seek(oldPos);
    }
