    public static void player(byte[] buffer, AudioFormat audioFormat) {
        SourceDataLine line = null;
        DataLine.Info info = null;
        byte[] abData = new byte[5000];
        int quant = buffer.length / abData.length;
        int readByte = 0;
        info = new DataLine.Info(SourceDataLine.class, audioFormat);
        try {
            line = (SourceDataLine) AudioSystem.getLine(info);
            line.open(audioFormat);
        } catch (LineUnavailableException e) {
            e.printStackTrace();
            System.exit(1);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        line.start();
        int n = 0;
        int fim = 0;
        while (n < quant + 1) {
            abData = Arrays.copyOfRange(buffer, fim, fim + abData.length - 1);
            readByte = line.write(abData, 0, abData.length);
            n++;
            fim += abData.length;
        }
    }
