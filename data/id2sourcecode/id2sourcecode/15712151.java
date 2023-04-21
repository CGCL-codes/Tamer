    @Deprecated
    public static ContentObject isContentInRepository(ContentObject availableContent, long timeout, CCNHandle handle) throws IOException {
        if (timeout == 0) {
            return null;
        }
        EnumeratedNameList enl = new EnumeratedNameList(availableContent.name(), handle);
        enl.waitForChildren(timeout);
        enl.stopEnumerating();
        if (enl.hasChild(availableContent.digest())) {
            return availableContent;
        }
        Log.info(Log.FAC_IO, "Repository does not contain expected child of {0}, has {1} children at that point", availableContent.name(), enl.childCount());
        return null;
    }
