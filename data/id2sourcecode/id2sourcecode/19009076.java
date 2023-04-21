    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        globalInstanceCount = in.readInt();
        tmpPath = (String) in.readObject();
        for (long i = (totalRemovedSize / elementsInMemory) + 2; i <= ((totalAddedSize - 1) / elementsInMemory) - 1; i++) {
            String filename = tmpPath + "arrayInst" + currentInstanceCount + "_tmpNum" + i + ".pma";
            writeArrayToFile((PacketArray) in.readObject(), filename);
        }
    }
