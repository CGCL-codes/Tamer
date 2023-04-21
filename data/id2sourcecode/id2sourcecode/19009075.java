    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeInt(globalInstanceCount);
        out.writeObject(tmpPath);
        for (long i = (totalRemovedSize / elementsInMemory) + 2; i <= ((totalAddedSize - 1) / elementsInMemory) - 1; i++) {
            String filename = tmpPath + "arrayInst" + currentInstanceCount + "_tmpNum" + i + ".pma";
            out.writeObject(readArrayFromFile(filename));
        }
    }
