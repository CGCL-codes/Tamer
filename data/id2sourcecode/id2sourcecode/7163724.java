    protected void testFile(boolean read, boolean write) throws IllegalAccessException, NullPointerException {
        if (this.m_File == null) throw new NullPointerException("This File-Object is null!");
        if (write && this.m_Writer == null) throw new IllegalAccessException("This File was opened read-only!");
    }
