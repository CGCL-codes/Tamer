    protected FileBasedDictionary(File _dicfile, String _encoding) throws IOException {
        this.dicfile = _dicfile;
        this.name = _dicfile.getName();
        indexFile = new File(dicfile.getCanonicalPath() + FileIndexContainer.EXTENSION);
        characterHandler = createCharacterHandler(_encoding);
        try {
            decoder = Charset.forName(_encoding).newDecoder();
        } catch (UnsupportedCharsetException ex) {
        }
        dicchannel = new FileInputStream(dicfile).getChannel();
        dictionarySize = (int) dicchannel.size();
        dictionary = dicchannel.map(FileChannel.MapMode.READ_ONLY, 0, dictionarySize);
        dictionaryDuplicate = dictionary.duplicate();
        binarySearchIndex = new BinarySearchIndex(BinarySearchIndex.TYPE);
        initSearchModes();
        initSupportedAttributes();
    }
