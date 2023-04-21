    public void closeReader(BitReader reader) throws IllegalArgumentException, BitStreamException {
        if (reader == null) throw new IllegalArgumentException("null reader");
        if (reader instanceof InputStreamBitReader) {
            try {
                ((InputStreamBitReader) reader).getInputStream().close();
            } catch (IOException e) {
                throw new BitStreamException(e);
            }
        } else if (reader instanceof FileChannelBitReader) {
            try {
                ((FileChannelBitReader) reader).getChannel().close();
            } catch (IOException e) {
                throw new BitStreamException(e);
            }
        }
    }
