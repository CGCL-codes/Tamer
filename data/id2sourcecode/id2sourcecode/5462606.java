    protected void checkAttributes() throws BuildException {
        assertTrue("The attribute 'parentDir' must indicate a valid directory.", parentDir != null && parentDir.isDirectory());
        assertTrue("You must have read and write access to " + parentDir.getAbsolutePath() + ".", parentDir.canRead() && parentDir.canWrite());
    }
