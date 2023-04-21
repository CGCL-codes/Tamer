    private Map<String, Integer> prepareNDMiniForRestart(long miniCheckpointPointer, OperatorID operatorID, int excessPackets) throws Throwable {
        Map<String, Integer> excessPacketMap = new HashMap<String, Integer>();
        excessPacketMap.put("input_1", new Integer(0));
        excessPacketMap.put("input_2", new Integer(0));
        String relativePathToMinis = "operators/operator_" + operatorID.getIDInt() + "/checkpoints/minis/mini_checkpoints";
        RandomAccessFile reader = new RandomAccessFile(_testProbe.getTestMethodOutputDirectory() + "run/" + relativePathToMinis, "r");
        String minisFilePath = _testProbe.getTestMethodOutputDirectory() + "restart/" + relativePathToMinis;
        File miniCPs = new File(minisFilePath);
        miniCPs.getParentFile().mkdirs();
        miniCPs.createNewFile();
        RandomAccessFile writer = new RandomAccessFile(miniCPs, "rw");
        reader.getChannel().transferTo(0, miniCheckpointPointer, writer.getChannel());
        FileChannel channel = reader.getChannel();
        channel.position(miniCheckpointPointer);
        ByteBuffer buffer = ByteBuffer.allocateDirect(5);
        int index = 0;
        int excess = excessPackets;
        while (true) {
            buffer.clear();
            channel.read(buffer);
            buffer.flip();
            byte port = buffer.get();
            int xcessPackets = buffer.getInt();
            index += 5;
            excess = excess - xcessPackets;
            String portName = port == 0x0001 ? "input_1" : "input_2";
            if (excess <= 0) {
                xcessPackets = xcessPackets + excess;
                excessPacketMap.put(portName, excessPacketMap.get(portName) + xcessPackets);
                break;
            } else {
                excessPacketMap.put(portName, excessPacketMap.get(portName) + xcessPackets);
            }
        }
        reader.getChannel().transferTo(miniCheckpointPointer, index, writer.getChannel());
        reader.close();
        writer.close();
        return excessPacketMap;
    }
