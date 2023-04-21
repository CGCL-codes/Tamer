    public TreeFileLeaf[] setChildren(EditableLeaf[] l_children) throws IOException {
        synchronized (treeFile) {
            treeFile.file.seek(treeFile.file.length());
            TreeFileLeaf[] result = new TreeFileLeaf[l_children.length];
            if (l_children.length != 0) {
                for (int fileIndex = 0; fileIndex < l_children.length; fileIndex++) {
                    result[fileIndex] = new TreeFileLeaf(this.treeFile);
                    result[fileIndex].positionInFile = (int) treeFile.file.getChannel().position();
                    result[fileIndex].leaf = l_children[fileIndex];
                    byte[] leafValue = l_children[fileIndex].getLeafValue();
                    if (l_children[fileIndex].isBranch()) {
                        result[fileIndex].entryType = TreeFileLeaf.ENTRY_TYPE_FOLDER;
                        result[fileIndex].positionOfFileList = NO_FILES_MARKER;
                        treeFile.file.writeByte(result[fileIndex].entryType);
                        treeFile.file.writeInt(NO_FILES_MARKER);
                    } else {
                        result[fileIndex].entryType = TreeFileLeaf.ENTRY_TYPE_FILE;
                        result[fileIndex].positionOfFileList = FILE_MARKER;
                        treeFile.file.writeByte(result[fileIndex].entryType);
                    }
                    treeFile.file.writeInt(leafValue.length);
                    treeFile.file.write(leafValue);
                }
                this.positionOfFileList = (int) treeFile.file.getChannel().position();
                treeFile.file.writeByte(TreeFileLeaf.ENTRY_TYPE_FILE_LIST);
                treeFile.file.writeInt(l_children.length);
                for (int fileIndex = 0; fileIndex < l_children.length; fileIndex++) {
                    treeFile.file.writeInt(result[fileIndex].positionInFile);
                }
            } else {
                this.positionOfFileList = NO_FILES_MARKER;
            }
            treeFile.file.seek(this.positionInFile + 1);
            treeFile.file.writeInt(this.positionOfFileList);
            return result;
        }
    }
