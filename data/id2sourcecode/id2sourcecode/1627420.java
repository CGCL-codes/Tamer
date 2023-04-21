    private boolean isExcluded(String name) {
        return name.startsWith(ClassMetaobject.methodPrefix) || name.equals(classobjectAccessor) || name.equals(metaobjectSetter) || name.equals(metaobjectGetter) || name.startsWith(readPrefix) || name.startsWith(writePrefix);
    }
