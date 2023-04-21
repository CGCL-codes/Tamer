    public static long getV2TagSizeIfExists(File file) throws IOException {
        FileInputStream fis = null;
        FileChannel fc = null;
        ByteBuffer bb = null;
        try {
            fis = new FileInputStream(file);
            fc = fis.getChannel();
            bb = ByteBuffer.allocate(TAG_HEADER_LENGTH);
            fc.read(bb);
            bb.flip();
            if (bb.limit() < (TAG_HEADER_LENGTH)) {
                return 0;
            }
        } finally {
            if (fc != null) {
                fc.close();
            }
            if (fis != null) {
                fis.close();
            }
        }
        byte[] tagIdentifier = new byte[FIELD_TAGID_LENGTH];
        bb.get(tagIdentifier, 0, FIELD_TAGID_LENGTH);
        if (!(Arrays.equals(tagIdentifier, TAG_ID))) {
            return 0;
        }
        byte majorVersion = bb.get();
        if ((majorVersion != ID3v22Tag.MAJOR_VERSION) && (majorVersion != ID3v23Tag.MAJOR_VERSION) && (majorVersion != ID3v24Tag.MAJOR_VERSION)) {
            return 0;
        }
        bb.get();
        bb.get();
        int frameSize = ID3SyncSafeInteger.bufferToValue(bb);
        frameSize += TAG_HEADER_LENGTH;
        return frameSize;
    }
