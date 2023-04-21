    public int calculateCRC32() {
        crc.reset();
        crc.update(signature);
        crc.update(revision);
        crc.update(headerSize);
        crc.update(0);
        crc.update(0);
        crc.update(0);
        crc.update(0);
        crc.update(reserved1);
        crc.update(primaryLBA);
        crc.update(backupLBA);
        crc.update(firstUsableLBA);
        crc.update(lastUsableLBA);
        crc.update(diskGUID);
        crc.update(partitionEntryLBA);
        crc.update(numberOfPartitionEntries);
        crc.update(sizeOfPartitionEntry);
        crc.update(partitionEntryArrayCRC32);
        return (int) (crc.getValue() & 0xFFFFFFFF);
    }
