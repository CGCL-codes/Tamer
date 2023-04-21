    public void setAllUserThreadsDied() {
        setProperty("allUserThreadsDied", true);
        _writer.println("// Note: all user threads died");
        enableRecording(_includeTermSyncPoints);
    }
