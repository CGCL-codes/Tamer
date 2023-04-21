    public final List<Boolean> getWriteAllowed() {
        if (writeAllowed == null) {
            writeAllowed = new ArrayList<Boolean>();
            List<String> readOnlyPropNames = getReadOnlyPropertyNames();
            if (readOnlyPropNames == null) readOnlyPropNames = new ArrayList<String>();
            for (String pName : getPropertyNames()) {
                if (readOnlyPropNames.contains(pName)) writeAllowed.add(Boolean.FALSE); else writeAllowed.add(Boolean.TRUE);
            }
        }
        return writeAllowed;
    }
