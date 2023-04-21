    public void playSound() {
        QDataSet dep0 = (QDataSet) ds.property(QDataSet.DEPEND_0);
        UnitsConverter uc = UnitsConverter.getConverter(SemanticOps.getUnits(dep0).getOffsetUnits(), Units.seconds);
        float sampleRate = (float) (1. / uc.convert(dep0.value(1) - dep0.value(0)));
        DasLogger.getLogger(DasLogger.GRAPHICS_LOG).fine("sampleRate= " + sampleRate);
        AudioFormat audioFormat = new AudioFormat(sampleRate, 16, 1, true, true);
        buf = new byte[EXTERNAL_BUFFER_SIZE];
        buffer = ByteBuffer.wrap(buf);
        buffer.order(ByteOrder.BIG_ENDIAN);
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
        try {
            line = (SourceDataLine) AudioSystem.getLine(info);
            line.open(audioFormat);
            line.addLineListener(getLineListener());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        bufferInputIndex = 0;
        line.start();
        int i = 0;
        int ibuf = 0;
        while (i < dep0.length()) {
            double d = ds.value(i++);
            int b = (int) (65536 * (d - min) / (max - min)) - 32768;
            try {
                buffer.putShort(ibuf, (short) b);
            } catch (IndexOutOfBoundsException ex) {
                ex.printStackTrace();
            }
            ibuf += 2;
            if (ibuf == EXTERNAL_BUFFER_SIZE) {
                line.write(buf, 0, ibuf);
                try {
                    WritableByteChannel out = new FileOutputStream("/home/jbf/tmp/foo.bin").getChannel();
                    out.write(buffer);
                    buffer.flip();
                } catch (IOException ex) {
                    Logger.getLogger(Auralizor.class.getName()).log(Level.SEVERE, null, ex);
                }
                ibuf = 0;
            }
        }
        line.write(buf, 0, ibuf);
        try {
            WritableByteChannel out = new FileOutputStream("/home/jbf/tmp/foo.bin").getChannel();
            out.write(buffer);
            buffer.flip();
        } catch (IOException ex) {
            Logger.getLogger(Auralizor.class.getName()).log(Level.SEVERE, null, ex);
        }
        ibuf = 0;
        line.drain();
        line.close();
    }
