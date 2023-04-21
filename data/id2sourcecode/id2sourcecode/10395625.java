    public File chooseForWrite() {
        if (mWriting) {
            throw new IllegalStateException("uncommitted write already in progress");
        }
        if (!mReal.exists()) {
            try {
                mReal.createNewFile();
            } catch (IOException e) {
            }
        }
        if (mTemp.exists()) {
            mTemp.delete();
        }
        mWriting = true;
        return mTemp;
    }
