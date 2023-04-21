    static void play(final InputStream data_input_stream, AudioFormat output_format) throws LineUnavailableException {
        final SourceDataLine output_line = (SourceDataLine) AudioSystem.getLine(new DataLine.Info(SourceDataLine.class, output_format));
        output_line.open();
        output_line.start();
        int tBuff = 4 << 10;
        byte[] output_buffer = new byte[tBuff];
        try {
            while (data_input_stream.available() > 0) {
                int read = data_input_stream.read(output_buffer, 0, tBuff);
                output_line.write(output_buffer, 0, read);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        output_line.drain();
        output_line.close();
    }
