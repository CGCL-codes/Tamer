    public static void writeReadData(SFFReadData readData, OutputStream out) throws IOException {
        final short[] flowgramValues = readData.getFlowgramValues();
        ByteBuffer flowValues = ByteBuffer.allocate(flowgramValues.length * 2);
        for (int i = 0; i < flowgramValues.length; i++) {
            flowValues.putShort(flowgramValues[i]);
        }
        out.write(flowValues.array());
        out.write(readData.getFlowIndexPerBase());
        final String basecalls = readData.getBasecalls();
        out.write(basecalls.getBytes());
        out.write(readData.getQualities());
        int readDataLength = SFFUtil.getReadDataLength(flowgramValues.length, basecalls.length());
        int padding = SFFUtil.caclulatePaddedBytes(readDataLength);
        out.write(new byte[padding]);
        out.flush();
    }
