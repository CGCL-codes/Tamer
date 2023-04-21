    protected void genCodecField(Class<?> msg, Field f, StringBuilder read, StringBuilder write, ExternalizableFactory factory) {
        Class<?> f_type = f.getType();
        String f_name = "msg." + f.getName();
        if (f_type.equals(boolean.class)) {
            read.append("		" + f_name + " = in.readBoolean();\n");
            write.append("		out.writeBoolean(" + f_name + ");\n");
        } else if (f_type.equals(boolean[].class)) {
            read.append("		" + f_name + " = in.readBooleanArray();\n");
            write.append("		out.writeBooleanArray(" + f_name + ");\n");
        } else if (f_type.equals(byte.class)) {
            read.append("		" + f_name + " = in.readByte();\n");
            write.append("		out.writeByte(" + f_name + ");\n");
        } else if (f_type.equals(byte[].class)) {
            read.append("		" + f_name + " = in.readByteArray();\n");
            write.append("		out.writeByteArray(" + f_name + ");\n");
        } else if (f_type.equals(short.class)) {
            read.append("		" + f_name + " = in.readShort();\n");
            write.append("		out.writeShort(" + f_name + ");\n");
        } else if (f_type.equals(short[].class)) {
            read.append("		" + f_name + " = in.readShortArray();\n");
            write.append("		out.writeShortArray(" + f_name + ");\n");
        } else if (f_type.equals(char.class)) {
            read.append("		" + f_name + " = in.readChar();\n");
            write.append("		out.writeChar(" + f_name + ");\n");
        } else if (f_type.equals(char[].class)) {
            read.append("		" + f_name + " = in.readCharArray();\n");
            write.append("		out.writeCharArray(" + f_name + ");\n");
        } else if (f_type.equals(int.class)) {
            read.append("		" + f_name + " = in.readInt();\n");
            write.append("		out.writeInt(" + f_name + ");\n");
        } else if (f_type.equals(int[].class)) {
            read.append("		" + f_name + " = in.readIntArray();\n");
            write.append("		out.writeIntArray(" + f_name + ");\n");
        } else if (f_type.equals(long.class)) {
            read.append("		" + f_name + " = in.readLong();\n");
            write.append("		out.writeLong(" + f_name + ");\n");
        } else if (f_type.equals(long[].class)) {
            read.append("		" + f_name + " = in.readLongArray();\n");
            write.append("		out.writeLongArray(" + f_name + ");\n");
        } else if (f_type.equals(float.class)) {
            read.append("		" + f_name + " = in.readFloat();\n");
            write.append("		out.writeFloat(" + f_name + ");\n");
        } else if (f_type.equals(float[].class)) {
            read.append("		" + f_name + " = in.readFloatArray();\n");
            write.append("		out.writeFloatArray(" + f_name + ");\n");
        } else if (f_type.equals(double.class)) {
            read.append("		" + f_name + " = in.readDouble();\n");
            write.append("		out.writeDouble(" + f_name + ");\n");
        } else if (f_type.equals(double[].class)) {
            read.append("		" + f_name + " = in.readDoubleArray();\n");
            write.append("		out.writeDoubleArray(" + f_name + ");\n");
        } else if (f_type.equals(String.class)) {
            read.append("		" + f_name + " = in.readUTF();\n");
            write.append("		out.writeUTF(" + f_name + ");\n");
        } else if (f_type.equals(String[].class)) {
            read.append("		" + f_name + " = in.readUTFArray();\n");
            write.append("		out.writeUTFArray(" + f_name + ");\n");
        } else if (Date.class.isAssignableFrom(f_type)) {
            read.append("		" + f_name + " = in.readDate(" + f_type.getCanonicalName() + ".class);\n");
            write.append("		out.writeDate(" + f_name + ");\n");
        } else if (ExternalizableMessage.class.isAssignableFrom(f_type)) {
            read.append("		" + f_name + " = in.readExternal(" + f_type.getCanonicalName() + ".class);\n");
            write.append("		out.writeExternal(" + f_name + ");\n");
        } else if (MutualMessage.class.isAssignableFrom(f_type)) {
            read.append("		" + f_name + " = in.readMutual(" + f_type.getCanonicalName() + ".class);\n");
            write.append("		out.writeMutual(" + f_name + ");\n");
        } else if (f_type.isArray()) {
            if (f_type.getComponentType().isArray()) {
                String leaf_type = NetDataTypes.toTypeName(NetDataTypes.getArrayCompomentType(f_type, factory));
                read.append("		" + f_name + " = (" + f_type.getCanonicalName() + ")in.readAnyArray(" + f_type.getCanonicalName() + ".class, " + "NetDataTypes." + leaf_type + ");\n");
                write.append("		out.writeAnyArray(" + f_name + ", " + "NetDataTypes." + leaf_type + ");\n");
            } else {
                Class<?> comp_type = f_type.getComponentType();
                if (ExternalizableMessage.class.isAssignableFrom(comp_type)) {
                    read.append("		" + f_name + " = (" + f_type.getCanonicalName() + ")in.readExternalArray(" + comp_type.getCanonicalName() + ".class);\n");
                    write.append("		out.writeExternalArray(" + f_name + ");\n");
                } else if (MutualMessage.class.isAssignableFrom(comp_type)) {
                    read.append("		" + f_name + " = (" + f_type.getCanonicalName() + ")in.readMutualArray(" + comp_type.getCanonicalName() + ".class);\n");
                    write.append("		out.writeMutualArray(" + f_name + ");\n");
                }
            }
        } else if (Collection.class.isAssignableFrom(f_type)) {
            Class argType = ReflectUtil.getFieldGenericType(f, 0);
            byte compType = NetDataTypes.getNetType(argType, factory);
            read.append("		" + f_name + " = (" + f_type.getCanonicalName() + ")in.readCollection(" + f_type.getCanonicalName() + ".class, " + "NetDataTypes." + NetDataTypes.toTypeName(compType) + ");\n");
            write.append("		out.writeCollection(" + f_name + ", " + "NetDataTypes." + NetDataTypes.toTypeName(compType) + ");\n");
        } else if (Map.class.isAssignableFrom(f_type)) {
            Class keyType = ReflectUtil.getFieldGenericType(f, 0);
            byte keyNetType = NetDataTypes.getNetType(keyType, factory);
            Class valueType = ReflectUtil.getFieldGenericType(f, 1);
            byte valueNetType = NetDataTypes.getNetType(valueType, factory);
            read.append("		" + f_name + " = (" + f_type.getCanonicalName() + ")in.readMap(" + f_type.getCanonicalName() + ".class, " + "NetDataTypes." + NetDataTypes.toTypeName(keyNetType) + ", " + "NetDataTypes." + NetDataTypes.toTypeName(valueNetType) + ");\n");
            write.append("		out.writeMap(" + f_name + ", " + "NetDataTypes." + NetDataTypes.toTypeName(keyNetType) + ", " + "NetDataTypes." + NetDataTypes.toTypeName(valueNetType) + ");\n");
        } else {
            read.append("		Unsupported type : " + f_name + " " + f_type.getName() + "\n");
            write.append("		Unsupported type : " + f_name + " " + f_type.getName() + "\n");
        }
    }
