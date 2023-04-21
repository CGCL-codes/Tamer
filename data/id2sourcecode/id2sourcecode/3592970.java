    public synchronized TreeFileLeaf createNew(File l_file, EditableLeaf l_root) throws IOException {
        if (file != null) {
            throw new IOException(RESMAP.getString("message.cannotCreateFromExistingFile.text"));
        }
        if (l_file.exists()) {
            l_file.delete();
        }
        treeFile = l_file;
        file = new RandomAccessFile(l_file, "rw");
        saveTimer.schedule(saveTimerTask, 300000, 300000);
        fileVersion = CURRENT_FILE_VERSION;
        file.writeInt(CURRENT_FILE_VERSION);
        file.writeInt(0);
        file.writeByte(TreeFileLeaf.ENTRY_TYPE_FILE_LIST);
        file.writeInt(1);
        file.writeInt((int) file.getChannel().position() + 4);
        TreeFileLeaf result = new TreeFileLeaf(this);
        result.entryType = TreeFileLeaf.ENTRY_TYPE_FOLDER;
        result.positionInFile = (int) file.getChannel().position();
        result.positionOfFileList = TreeFileLeaf.NO_FILES_MARKER;
        result.leaf = l_root;
        file.writeByte(result.entryType);
        file.writeInt(result.positionOfFileList);
        byte[] leafValue = l_root.getLeafValue();
        file.writeInt(leafValue.length);
        file.write(leafValue);
        return result;
    }
