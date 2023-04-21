    private final boolean attributeHasChanged(FileStats s, File f) {
        return f.canRead() != s.readable || f.canWrite() != s.writeable || f.isHidden() != s.hidden;
    }
