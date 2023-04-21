    @Override
    public void writeFixedPart(LocalObjectContainer file, boolean startFileLockingThread, boolean shuttingDown, StatefulBuffer writer, int blockSize) {
        SystemData systemData = file.systemData();
        writer.append(SIGNATURE);
        writer.writeByte(version());
        writer.writeInt((int) timeToWrite(_timerFileLock.openTime(), shuttingDown));
        writer.writeLong(timeToWrite(_timerFileLock.openTime(), shuttingDown));
        writer.writeLong(timeToWrite(System.currentTimeMillis(), shuttingDown));
        writer.writeInt(blockSize);
        writer.writeInt(systemData.classCollectionID());
        writer.writeByte(systemData.idSystemType());
        writer.writeInt(((FileHeaderVariablePart2) _variablePart).address());
        writer.writeInt(((FileHeaderVariablePart2) _variablePart).length());
        writer.writeInt(_transactionPointerAddress);
        if (Debug4.xbytes) {
            writer.checkXBytes(false);
        }
        writer.write();
        if (shuttingDown) {
            writeVariablePart(file, true);
        } else {
            file.syncFiles();
        }
        if (startFileLockingThread) {
            file.threadPool().start("db4o lock thread", _timerFileLock);
        }
    }
