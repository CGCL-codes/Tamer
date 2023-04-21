    public void addSource(Source src, boolean allowOverwrite) throws AlreadyExistingException {
        src.setResource(this);
        if (!allowOverwrite && sources.contains(src)) {
            throw new AlreadyExistingException();
        }
        if (allowOverwrite && sources.contains(src)) {
            sources.remove(src);
            for (ExtensionMapping ext : this.getMappings()) {
                if (ext.getSource().equals(src)) {
                    ext.setSource(src);
                }
            }
        }
        sources.add(src);
    }
