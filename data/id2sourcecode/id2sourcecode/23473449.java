    private int writeTransformedWaveletDelta(TransformedWaveletDelta delta) throws IOException {
        long startingPosition = file.getFilePointer();
        ProtoTransformedWaveletDelta protoDelta = ProtoDeltaStoreDataSerializer.serialize(delta);
        OutputStream stream = Channels.newOutputStream(file.getChannel());
        protoDelta.writeTo(stream);
        return (int) (file.getFilePointer() - startingPosition);
    }
