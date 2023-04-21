    public void writeFixedPart(LocalObjectContainer file, boolean startFileLockingThread, boolean shuttingDown, StatefulBuffer writer, int blockSize_, int freespaceID) {
        writer.writeByte(Const4.YAPFILEVERSION);
        writer.writeByte((byte) blockSize_);
        writer.writeInt(_configBlock.address());
        writer.writeInt((int) timeToWrite(_configBlock.openTime(), shuttingDown));
        writer.writeInt(file.systemData().classCollectionID());
        writer.writeInt(freespaceID);
        if (Debug.xbytes && Deploy.overwrite) {
            writer.setID(Const4.IGNORE_ID);
        }
        writer.write();
        file.syncFiles();
    }
