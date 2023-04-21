    public static int writeReadData(SffReadData readData, OutputStream out) throws IOException {
        final short[] flowgramValues = readData.getFlowgramValues();
        ByteBuffer flowValues = ByteBuffer.allocate(flowgramValues.length * 2);
        for (int i = 0; i < flowgramValues.length; i++) {
            flowValues.putShort(flowgramValues[i]);
        }
        out.write(flowValues.array());
        out.write(readData.getFlowIndexPerBase());
        final NucleotideSequence basecalls = readData.getBasecalls();
        out.write(basecalls.toString().getBytes(IOUtil.UTF_8));
        out.write(readData.getQualities());
        int readDataLength = SffUtil.getReadDataLength(flowgramValues.length, (int) basecalls.getLength());
        int padding = SffUtil.caclulatePaddedBytes(readDataLength);
        out.write(new byte[padding]);
        out.flush();
        return readDataLength + padding;
    }
