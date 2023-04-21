    @Test
    public void testEndWithHighSurrogate() throws IOException {
        int scp = 0x00010400;
        String base = "本日は晴天なり";
        char highSurrogate = Character.toChars(scp)[0];
        assertTrue(Character.isHighSurrogate(highSurrogate));
        CharArrayWriter writer = new CharArrayWriter();
        writer.write(base);
        writer.write(highSurrogate);
        int[] answer = getCodePoints(base + (char) BasicCodePointReader.DEFAULT_ALTERNATION_CODEPOINT);
        long[] positions = getPositions(base + (char) BasicCodePointReader.DEFAULT_ALTERNATION_CODEPOINT);
        CodePointReader reader = new BasicCodePointReader(new CharArrayReader(writer.toCharArray()));
        if (!match(reader, answer, positions)) {
            fail("コードポイントが一致しません。");
        }
    }
