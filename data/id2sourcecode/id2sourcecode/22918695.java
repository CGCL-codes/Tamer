    public void store(Comparable id, Serializable obj, Continuation c) {
        try {
            if (id == null || obj == null) {
                c.receiveResult(new Boolean(false));
                return;
            }
            File objFile = getFile(id);
            File transcFile = makeFile(id);
            writeObject(obj, id, readVersion(objFile) + 1, transcFile);
            if (getUsedSpace() + getFileLength(objFile) > getStorageSize()) {
                deleteFile(transcFile);
                c.receiveResult(new Boolean(false));
                return;
            } else {
                decreaseUsedSpace(getFileLength(objFile));
                deleteFile(objFile);
                increaseUsedSpace(transcFile.length());
                createMapping(id, transcFile);
            }
            c.receiveResult(new Boolean(true));
        } catch (Exception e) {
            e.printStackTrace();
            c.receiveException(e);
        }
    }
