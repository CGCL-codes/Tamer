    private static void writeValue(DataOutputStream os, Object obj, boolean tagged) throws IOException, JdwpException {
        Class clazz = obj.getClass();
        if (clazz.isPrimitive()) {
            if (clazz == byte.class) {
                if (tagged) os.writeByte(JdwpConstants.Tag.BYTE);
                os.writeByte(((Byte) obj).byteValue());
            } else if (clazz == char.class) {
                if (tagged) os.writeByte(JdwpConstants.Tag.CHAR);
                os.writeChar(((Character) obj).charValue());
            } else if (clazz == float.class) {
                if (tagged) os.writeByte(JdwpConstants.Tag.FLOAT);
                os.writeFloat(((Float) obj).floatValue());
            } else if (clazz == double.class) {
                if (tagged) os.writeByte(JdwpConstants.Tag.DOUBLE);
                os.writeDouble(((Double) obj).doubleValue());
            } else if (clazz == int.class) {
                if (tagged) os.writeByte(JdwpConstants.Tag.BYTE);
                os.writeInt(((Integer) obj).intValue());
            } else if (clazz == long.class) {
                if (tagged) os.writeByte(JdwpConstants.Tag.LONG);
                os.writeLong(((Long) obj).longValue());
            } else if (clazz == short.class) {
                if (tagged) os.writeByte(JdwpConstants.Tag.SHORT);
                os.writeInt(((Short) obj).shortValue());
            } else if (clazz == void.class) {
                if (tagged) os.writeByte(JdwpConstants.Tag.VOID);
            } else if (clazz == boolean.class) {
                if (tagged) os.writeByte(JdwpConstants.Tag.BOOLEAN);
                os.writeBoolean(((Boolean) obj).booleanValue());
            } else {
                throw new JdwpInternalErrorException("Field has invalid primitive!");
            }
        } else {
            if (tagged) {
                if (clazz.isArray()) os.writeByte(JdwpConstants.Tag.ARRAY); else if (obj instanceof String) os.writeByte(JdwpConstants.Tag.STRING); else if (obj instanceof Thread) os.writeByte(JdwpConstants.Tag.THREAD); else if (obj instanceof ThreadGroup) os.writeByte(JdwpConstants.Tag.THREAD_GROUP); else if (obj instanceof ClassLoader) os.writeByte(JdwpConstants.Tag.CLASS_LOADER); else if (obj instanceof Class) os.writeByte(JdwpConstants.Tag.CLASS_OBJECT); else os.writeByte(JdwpConstants.Tag.OBJECT);
            }
            ObjectId oid = VMIdManager.getDefault().getObjectId(obj);
            oid.write(os);
        }
    }
