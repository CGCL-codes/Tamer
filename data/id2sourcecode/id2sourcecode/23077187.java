    public static void main(String[] args) {
        CAppBridge.init();
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream(10240);
            UncachedUrlStream os = new UncachedUrlStream(CIO.getResourceURL("broken_glass.ogg"));
            for (Object los : os.getLogicalStreams()) {
                LogicalOggStream loStream = (LogicalOggStream) los;
                VorbisStream vStream = new VorbisStream(loStream);
                IdentificationHeader vStreamHdr = vStream.getIdentificationHeader();
                AudioFormat audioFormat = new AudioFormat((float) vStreamHdr.getSampleRate(), 16, vStreamHdr.getChannels(), true, true);
                System.out.println(audioFormat);
                try {
                    byte[] data = new byte[1];
                    while (true) {
                        vStream.readPcm(data, 0, 1);
                        baos.write(data);
                    }
                } catch (EndOfOggStreamException e) {
                }
                vStream.close();
                loStream.close();
            }
            os.close();
            System.out.println("pcm data size = " + baos.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
