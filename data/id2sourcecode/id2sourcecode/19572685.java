    protected void genCodecField(ExternalizableFactory factory, Class<?> msg, Field f, StringBuilder read, StringBuilder write) {
        Class<?> f_type = f.getType();
        String f_name = "msg." + f.getName();
        if (f_type.equals(boolean.class)) {
            read.append("		" + f_name + " = input.readBoolean();\n");
            write.append("		output.writeBoolean(" + f_name + ");\n");
        } else if (f_type.equals(boolean[].class)) {
            read.append("		" + f_name + " = input.readBooleanArray();\n");
            write.append("		output.writeBooleanArray(" + f_name + ");\n");
        } else if (f_type.equals(byte.class)) {
            read.append("		" + f_name + " = input.readByte();\n");
            write.append("		output.writeByte(" + f_name + ");\n");
        } else if (f_type.equals(byte[].class)) {
            read.append("		" + f_name + " = input.readByteArray();\n");
            write.append("		output.writeByteArray(" + f_name + ");\n");
        } else if (f_type.equals(short.class)) {
            read.append("		" + f_name + " = input.readShort();\n");
            write.append("		output.writeShort(" + f_name + ");\n");
        } else if (f_type.equals(short[].class)) {
            read.append("		" + f_name + " = input.readShortArray();\n");
            write.append("		output.writeShortArray(" + f_name + ");\n");
        } else if (f_type.equals(char.class)) {
            read.append("		" + f_name + " = input.readChar();\n");
            write.append("		output.writeChar(" + f_name + ");\n");
        } else if (f_type.equals(char[].class)) {
            read.append("		" + f_name + " = input.readCharArray();\n");
            write.append("		output.writeCharArray(" + f_name + ");\n");
        } else if (f_type.equals(int.class)) {
            read.append("		" + f_name + " = input.readInt();\n");
            write.append("		output.writeInt(" + f_name + ");\n");
        } else if (f_type.equals(int[].class)) {
            read.append("		" + f_name + " = input.readIntArray();\n");
            write.append("		output.writeIntArray(" + f_name + ");\n");
        } else if (f_type.equals(float.class)) {
            read.append("		" + f_name + " = input.readFloat();\n");
            write.append("		output.writeFloat(" + f_name + ");\n");
        } else if (f_type.equals(float[].class)) {
            read.append("		" + f_name + " = input.readFloatArray();\n");
            write.append("		output.writeFloatArray(" + f_name + ");\n");
        } else if (f_type.equals(double.class)) {
            read.append("		" + f_name + " = input.readDouble();\n");
            write.append("		output.writeDouble(" + f_name + ");\n");
        } else if (f_type.equals(double[].class)) {
            read.append("		" + f_name + " = input.readDoubleArray();\n");
            write.append("		output.writeDoubleArray(" + f_name + ");\n");
        } else if (f_type.equals(String.class)) {
            read.append("		" + f_name + " = input.readJavaUTF();\n");
            write.append("		output.writeJavaUTF(" + f_name + ");\n");
        } else if (f_type.equals(String[].class)) {
            read.append("		" + f_name + " = input.readUTFArray();\n");
            write.append("		output.writeUTFArray(" + f_name + ");\n");
        } else if (Date.class.isAssignableFrom(f_type)) {
            read.append("		" + f_name + " = input.readDate();\n");
            write.append("		output.writeDate(" + f_name + ");\n");
        } else if (MutualMessage.class.isAssignableFrom(f_type)) {
            read.append("		" + f_name + " = input.readMutual() as " + f_type.getCanonicalName() + ";\n");
            write.append("		output.writeMutual(" + f_name + ");\n");
        } else if (f_type.isArray()) {
            if (f_type.getComponentType().isArray()) {
                String leaf_type = NetDataTypes.toTypeName(NetDataTypes.getArrayCompomentType(f_type, factory));
                read.append("		" + f_name + " = input.readAnyArray(" + "NetDataTypes." + leaf_type + ");\n");
                write.append("		output.writeAnyArray(" + f_name + ", " + "NetDataTypes." + leaf_type + ");\n");
            } else {
                read.append("		" + f_name + " = input.readMutualArray();\n");
                write.append("		output.writeMutualArray(" + f_name + ");\n");
            }
        } else if (Collection.class.isAssignableFrom(f_type)) {
            Class argType = ReflectUtil.getFieldGenericType(f, 0);
            byte compType = NetDataTypes.getNetType(argType, factory);
            read.append("		" + f_name + " = input.readCollection(" + "NetDataTypes." + NetDataTypes.toTypeName(compType) + ");\n");
            write.append("		output.writeCollection(" + f_name + ", " + "NetDataTypes." + NetDataTypes.toTypeName(compType) + ");\n");
        } else if (Map.class.isAssignableFrom(f_type)) {
            Class keyType = ReflectUtil.getFieldGenericType(f, 0);
            byte keyNetType = NetDataTypes.getNetType(keyType, factory);
            Class valueType = ReflectUtil.getFieldGenericType(f, 1);
            byte valueNetType = NetDataTypes.getNetType(valueType, factory);
            read.append("		" + f_name + " = input.readMap(" + "NetDataTypes." + NetDataTypes.toTypeName(keyNetType) + ", " + "NetDataTypes." + NetDataTypes.toTypeName(valueNetType) + ");\n");
            write.append("		output.writeMap(" + f_name + ", " + "NetDataTypes." + NetDataTypes.toTypeName(keyNetType) + ", " + "NetDataTypes." + NetDataTypes.toTypeName(valueNetType) + ");\n");
        } else {
            read.append("		Unsupported type : " + f_name + " " + f_type.getName() + "\n");
            write.append("		Unsupported type : " + f_name + " " + f_type.getName() + "\n");
        }
    }
