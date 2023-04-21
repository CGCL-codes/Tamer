    public Packet remove() {
        Packet returnPacket = elementAt(0);
        totalRemovedSize++;
        if ((totalRemovedSize % elementsInMemory) == 0) {
            if (totalRemovedSize != 0) {
                readArray.clear();
                PacketArray temp = readArray;
                readArray = readBuffer;
                readBuffer = temp;
                if (currentWritePosition < 2 * elementsInMemory) {
                    ;
                } else if (currentWritePosition <= 3 * elementsInMemory) {
                    readBuffer.clear();
                    temp = readBuffer;
                    readBuffer = writeArray;
                    writeArray = temp;
                } else {
                    long arrayNum = (totalRemovedSize / elementsInMemory) + 1;
                    String filename = tmpPath + "arrayInst" + currentInstanceCount + "_tmpNum" + arrayNum + ".pma";
                    readBuffer = readArrayFromFile(filename);
                    (new File(filename)).delete();
                }
                currentWritePosition -= elementsInMemory;
            }
        }
        if (returnPacket == null) {
        }
        return returnPacket;
    }
