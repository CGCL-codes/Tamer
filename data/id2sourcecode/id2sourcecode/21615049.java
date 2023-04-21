    public String toString() {
        String access;
        if (isReadable()) {
            if (isWritable()) access = "read/write"; else access = "read-only";
        } else if (isWritable()) access = "write-only"; else access = "no-access";
        return getClass().getName() + "[" + "description=" + getDescription() + ", " + "name=" + getName() + ", " + "type=" + getType() + ", " + access + ", " + (isIs() ? "isIs, " : "") + "descriptor=" + getDescriptor() + "]";
    }
