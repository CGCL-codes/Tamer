    @Test
    public void testWrite_3args_1() throws Exception {
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
        Type fileType = new Type("WAV", "wav");
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        VorbisAudioFileWriter instance = new VorbisAudioFileWriter();
        try {
            instance.write(stream, fileType, out);
            fail("filetype shouldn't be accepted");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("not supported"));
        }
        fileType = new Type("OGG", "ogg");
        int written = AudioSystem.write(stream, fileType, out);
        assertTrue(written > 1);
        byte[] bytes = out.toByteArray();
        AudioInputStream ais = AudioSystem.getAudioInputStream(new ByteArrayInputStream(bytes));
        assertEquals("VORBISENC", ais.getFormat().getEncoding().toString());
        AudioInputStream aisConverted = AudioSystem.getAudioInputStream(new AudioFormat(44100, 16, 2, true, false), ais);
        System.out.println(aisConverted.getFormat());
        int c = aisConverted.read();
        int readCount = 0;
        while (c != -1) {
            c = aisConverted.read();
            readCount++;
        }
        System.out.println("Original stream was " + writeCount[0] + " bytes, new stream is " + readCount + " bytes");
        assertTrue(writeCount[0] < readCount);
    }
