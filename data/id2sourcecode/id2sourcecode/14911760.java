    @Test
    public void testWrite_3args_2() throws Exception {
        System.out.println("write");
        final int writeCount[] = { 0 };
        InputStream simpleSineInput = new InputStream() {

            @Override
            public int read() throws IOException {
                if (writeCount[0] % 2 == 0) {
                    writeCount[0]++;
                    return 0;
                } else return writeCount[0]++ % (44100 / 220) > 50 ? 100 : -100;
            }
        };
        AudioInputStream stream = new AudioInputStream(simpleSineInput, new AudioFormat(44100, 16, 2, true, false), 88200);
        Type fileType = new Type("OGG", "ogg");
        File out = new File("test.ogg");
        int result = AudioSystem.write(stream, fileType, out);
        assertTrue(result > 1);
        AudioInputStream ais = AudioSystem.getAudioInputStream(out);
        assertEquals("VORBISENC", ais.getFormat().getEncoding().toString());
        AudioInputStream aisConverted = AudioSystem.getAudioInputStream(new AudioFormat(44100, 16, 2, true, false), ais);
        int c = aisConverted.read();
        int readCount = 0;
        while (c != -1) {
            c = aisConverted.read();
            readCount++;
        }
        System.out.println("Original stream was " + writeCount[0] + " bytes, new stream is " + readCount + " bytes");
        assertTrue(writeCount[0] < readCount);
    }
