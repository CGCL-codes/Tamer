    protected BaseProperty(ClassInfo<C> classInfo, String name, Class<T> type, boolean readable, boolean writeable) {
        ivClassInfo = classInfo;
        ivType = type;
        ivName = name;
        ivReadable = readable;
        ivWriteable = writeable;
    }
