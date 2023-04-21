    private static final String resolveAttributeAccess(final MethodDoc gMethod, final MethodDoc sMethod) {
        if (gMethod != null) {
            return (null == sMethod) ? "read-only" : "read-write";
        } else {
            return (null == sMethod) ? null : "write-only";
        }
    }
