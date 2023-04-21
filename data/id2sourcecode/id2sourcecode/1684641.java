    public static HashMap<ByteArrayHasher, MutableIntPair> readNioIndexB(String fileName, int sizeFile) throws IOException {
        HashMap<ByteArrayHasher, MutableIntPair> hash = new HashMap<ByteArrayHasher, MutableIntPair>();
        FileChannel fcIdx = new FileInputStream(fileName).getChannel();
        MutableIntPair lastPos = null;
        MappedByteBuffer map = fcIdx.map(FileChannel.MapMode.READ_ONLY, 0, new File(fileName).length());
        while (map.hasRemaining()) {
            int position = map.getInt();
            int n = map.get();
            ByteBuffer bb = ByteBuffer.allocate(LanguageConstants.SIZEOF_INT * n);
            for (int i = 0; i < n; i++) {
                int idxInVocabulary = NioBuffers.decodeVariableLengthInteger(map);
                bb.putInt(idxInVocabulary);
            }
            if (lastPos != null) lastPos.second = position;
            lastPos = new MutableIntPair(position, -1);
            hash.put(new ByteArrayHasher(bb.array()), lastPos);
        }
        fcIdx.close();
        if (lastPos != null) lastPos.second = sizeFile;
        System.err.println("Loaded " + hash.size() + " entries from " + fileName);
        return hash;
    }
