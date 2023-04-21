    @Test
    public void recognizeTiffImage() throws IOException {
        File tiff = getTiffFile();
        MappedByteBuffer buf = new FileInputStream(tiff).getChannel().map(MapMode.READ_ONLY, 0, tiff.length());
        Tesjeract tess = new Tesjeract("eng");
        EANYCodeChar[] words = tess.recognizeAllWords(buf);
        assertEquals("There should be 352 chars in sample tiff image", 352, words.length);
        int i = 0;
        for (char c : "The(quick)".toCharArray()) {
            assertEquals((int) c, words[i++].char_code);
        }
    }
