    @Override
    public void setFile(File pdb, boolean headerOnly) throws IOException {
        mFile = pdb;
        mPage = 0;
        FileChannel channel = new FileInputStream(pdb).getChannel();
        byte[] nameByte = new byte[32];
        channel.map(MapMode.READ_ONLY, 0, 32).get(nameByte);
        mName = new String(nameByte, mEncode).replace('_', ' ').trim();
        byte[] fourBytes = new byte[4];
        channel.map(MapMode.READ_ONLY, 60, 4).get(fourBytes);
        String type = new String(fourBytes);
        channel.map(MapMode.READ_ONLY, 64, 4).get(fourBytes);
        String creatorID = new String(fourBytes);
        if (type.equals(PalmDocDB.PALMDOC_TYPE_ID) && creatorID.equals(PalmDocDB.PALMDOC_CREATOR_ID)) {
            setFormat(2);
        } else if (type.equals("PNRd") && creatorID.equals("PPrs")) {
            mType = TYPE_EREADER;
            setFormat(1);
            setEncode("US-ASCII");
        } else if (type.equals("zTXT") && creatorID.equals("GPlm")) {
            setFormat(3);
            setEncode("UTF-8");
        } else if (creatorID.equals("MTIT")) {
            setEncode("Big5");
            mType = TYPE_Hodoo;
        } else if (creatorID.equals("MTIU")) {
            setEncode("UTF-16LE");
            mType = TYPE_Hodoo;
        } else if (creatorID.equals("SilX")) {
            throw new FormatNotSupportException("iSilo");
        }
        mCount = channel.map(MapMode.READ_ONLY, 76, 2).asCharBuffer().get();
        int offset = 78;
        if (mType == TYPE_EREADER) {
            offset += 8;
        }
        mRecodeOffset = new int[mCount];
        for (int i = 0; i < mCount; i++) {
            mRecodeOffset[i] = channel.map(MapMode.READ_ONLY, offset, 4).asIntBuffer().get();
            offset += 8;
        }
        if (mType == TYPE_Hodoo) {
            byte[] fifityBytes = new byte[50];
            channel.map(MapMode.READ_ONLY, mRecodeOffset[0], fifityBytes.length).order(ByteOrder.BIG_ENDIAN).get(fifityBytes);
            String str = new String(fifityBytes, mEncode);
            mName = str.substring(0, str.indexOf(27, 0)).trim();
        }
        channel.close();
    }
