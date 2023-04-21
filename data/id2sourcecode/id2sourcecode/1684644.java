    public static HashMap<Object, Integer> readNioIndex(String fileName, FToken[] idToFToken, EFProcessorIf processor, TranslationTable tt) throws IOException {
        HashMap<Object, Integer> hash = new HashMap<Object, Integer>();
        FileChannel fcIdx = new FileInputStream(fileName).getChannel();
        MappedByteBuffer map = fcIdx.map(FileChannel.MapMode.READ_ONLY, 0, new File(fileName).length());
        while (map.hasRemaining()) {
            int position = map.getInt();
            int n = map.get();
            FToken[] f = new FToken[n];
            for (int i = 0; i < f.length; i++) {
                int idxInVocabulary = NioBuffers.decodeVariableLengthInteger(map);
                f[i] = idToFToken[idxInVocabulary];
            }
            hash.put(processor.getKey(f, tt), position);
        }
        fcIdx.close();
        System.err.println("Loaded " + hash.size() + " entries from " + fileName);
        return hash;
    }
