    void writeAll(final DataOutput dataOutput) throws IOException {
        writeVersion(dataOutput);
        writeModifiedDate(dataOutput);
        writeRecordCount(dataOutput);
        writeHeaderLength(dataOutput);
        writeRecordLength(dataOutput);
        writeZeros(dataOutput, LENGTH_RESERVED_1);
        writeIncompleteTransaction(dataOutput);
        writeEncryptionFlag(dataOutput);
        writeFreeRecordThread(dataOutput);
        writeZeros(dataOutput, LENGTH_RESERVED_2);
        writeMdxFlag(dataOutput);
        writeLanguageDriver(dataOutput);
        writeZeros(dataOutput, LENGTH_RESERVED_3);
        writeFieldDescriptors(dataOutput);
    }
