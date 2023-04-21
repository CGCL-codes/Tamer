    public synchronized void saveLeafValue() throws IOException {
        synchronized (treeFile) {
            treeFile.file.seek(this.positionInFile + 1);
            if (this.isBranch()) {
                treeFile.file.skipBytes(4);
            }
            int lengthOfExistingEntry = treeFile.file.readInt();
            byte[] leafValue = leaf.getLeafValue();
            if (leafValue.length <= lengthOfExistingEntry) {
                treeFile.file.seek(this.treeFile.file.getChannel().position() - 4);
                treeFile.file.writeInt(leafValue.length);
                treeFile.file.write(leafValue);
            } else {
                treeFile.file.seek(this.positionInFile);
                treeFile.file.writeInt(ENTRY_TYPE_MOVED);
                this.positionInFile = (int) treeFile.file.length();
                treeFile.file.writeInt(this.positionInFile);
                treeFile.file.seek(this.positionInFile);
                treeFile.file.writeByte(this.entryType);
                if (this.isBranch()) {
                    treeFile.file.writeInt(this.positionOfFileList);
                }
                treeFile.file.writeInt(leafValue.length);
                treeFile.file.write(leafValue);
            }
        }
    }
