    public static void release() {
        saveInfo();
        mRunFlag = false;
        mLogFile.Close();
        if (writeFileThread != null) {
            writeFileThread = null;
        }
    }
