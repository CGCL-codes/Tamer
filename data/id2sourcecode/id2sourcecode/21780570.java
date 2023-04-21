    private void consoleSumOutput(int numberOfPackets, int packetBytes) {
        consoleStream.write("Packet: " + numberOfPackets + "\n");
        consoleStream.write("Bytes this packet: " + packetBytes + "\n");
        consoleStream.write("Sum of readed bytes: " + numberOfPackets + "\n");
        consoleStream.write("------------------------------------------\n");
    }
