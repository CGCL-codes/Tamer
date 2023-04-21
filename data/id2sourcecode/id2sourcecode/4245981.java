    private static void playPart(int count) throws Exception {
        final String fname = "C:\\proj\\cr6\\sounds/onetwothree.voice";
        DataInputStream di = new DataInputStream(new FileInputStream(fname));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        for (int i = 0; i < count; i++) {
            dos.writeShort(di.readShort());
        }
        SourceDataLine line = AudioSystem.getSourceDataLine(new AudioFormat(11025, 16, 1, true, true));
        line.open();
        line.start();
        line.write(baos.toByteArray(), 0, baos.size());
    }
