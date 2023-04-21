    private void validateNDMergeMiniCheckpointPointer(OperatorID operatorID, long miniCheckpointPointer) throws IOException {
        String relativePathToMinis = "operators/operator_" + operatorID.getIDInt() + "/checkpoints/minis/mini_checkpoints";
        RandomAccessFile reader = new RandomAccessFile(_testProbe.getTestMethodOutputDirectory() + "run/" + relativePathToMinis, "r");
        FileChannel channel = reader.getChannel();
        ByteBuffer buffer = ByteBuffer.allocate(5);
        int seenPackets = 0;
        while (seenPackets < _packetsCommittedAtRestartCP) {
            buffer.clear();
            channel.read(buffer);
            buffer.flip();
            buffer.get();
            seenPackets = seenPackets + buffer.getInt();
        }
        assertTrue("seenPackets: " + seenPackets + " packetsCommittedAtRestartCP: " + _packetsCommittedAtRestartCP, seenPackets == _packetsCommittedAtRestartCP);
        assertTrue("channel.position(): " + channel.position() + " miniCheckpointPointer:" + miniCheckpointPointer, channel.position() == miniCheckpointPointer);
        channel.close();
        reader.close();
    }
