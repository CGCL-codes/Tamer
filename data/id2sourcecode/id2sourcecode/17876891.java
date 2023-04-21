    private void executeGetValues(ByteBuffer bb, DataOutputStream os) throws JdwpException, IOException {
        ObjectId oid = idMan.readObjectId(bb);
        Object array = oid.getObject();
        int first = bb.getInt();
        int length = bb.getInt();
        Class clazz = array.getClass().getComponentType();
        if (clazz == byte.class) os.writeByte(JdwpConstants.Tag.BYTE); else if (clazz == char.class) os.writeByte(JdwpConstants.Tag.CHAR); else if (clazz == float.class) os.writeByte(JdwpConstants.Tag.FLOAT); else if (clazz == double.class) os.writeByte(JdwpConstants.Tag.DOUBLE); else if (clazz == int.class) os.writeByte(JdwpConstants.Tag.BYTE); else if (clazz == long.class) os.writeByte(JdwpConstants.Tag.LONG); else if (clazz == short.class) os.writeByte(JdwpConstants.Tag.SHORT); else if (clazz == void.class) os.writeByte(JdwpConstants.Tag.VOID); else if (clazz == boolean.class) os.writeByte(JdwpConstants.Tag.BOOLEAN); else if (clazz.isArray()) os.writeByte(JdwpConstants.Tag.ARRAY); else if (String.class.isAssignableFrom(clazz)) os.writeByte(JdwpConstants.Tag.STRING); else if (Thread.class.isAssignableFrom(clazz)) os.writeByte(JdwpConstants.Tag.THREAD); else if (ThreadGroup.class.isAssignableFrom(clazz)) os.writeByte(JdwpConstants.Tag.THREAD_GROUP); else if (ClassLoader.class.isAssignableFrom(clazz)) os.writeByte(JdwpConstants.Tag.CLASS_LOADER); else if (Class.class.isAssignableFrom(clazz)) os.writeByte(JdwpConstants.Tag.CLASS_OBJECT); else os.writeByte(JdwpConstants.Tag.OBJECT);
        for (int i = first; i < first + length; i++) {
            Object value = Array.get(array, i);
            if (clazz.isPrimitive()) Value.writeUntaggedValue(os, value); else Value.writeTaggedValue(os, value);
        }
    }
