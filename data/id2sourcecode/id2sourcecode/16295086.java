    @Test
    public void digest() {
        for (Map.Entry<String, Integer> entry : _sentences.entrySet()) {
            String sentence = entry.getKey();
            int checksum = Checksum.digest(sentence);
            Assert.assertEquals(entry.getValue(), new Integer(checksum));
        }
    }
