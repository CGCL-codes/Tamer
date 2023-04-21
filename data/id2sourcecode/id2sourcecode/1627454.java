    @Test
    public void testEndWithLowSurrogate() throws IOException {
        int scp = 0x00010400;
        String base = "本日は晴天なり";
        char lowSurrogate = Character.toChars(scp)[1];
        assertTrue(Character.isLowSurrogate(lowSurrogate));
        CharArrayWriter writer = new CharArrayWriter();
        writer.write(base);
        writer.write(lowSurrogate);
        int[] answer = getCodePoints(base + (char) BasicCodePointReader.DEFAULT_ALTERNATION_CODEPOINT);
        long[] positions = getPositions(base + (char) BasicCodePointReader.DEFAULT_ALTERNATION_CODEPOINT);
        CodePointReader reader = new BasicCodePointReader(new CharArrayReader(writer.toCharArray()));
        if (!match(reader, answer, positions)) {
            fail("コードポイントが一致しません。");
        }
    }
