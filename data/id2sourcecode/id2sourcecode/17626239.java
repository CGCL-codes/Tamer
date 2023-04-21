    public JavaGenerator(List<GeneratedClass> pClassDescriptions, HashMap<String, GeneratedEnumeration> pEnumerations, String pDirectory, Properties pJavaProperties, String pLicenseStatement) {
        super(pClassDescriptions, pEnumerations, pDirectory, pJavaProperties, pLicenseStatement);
        types.put("unsigned byte", new PrimitiveTypeInfo("short", "Short", "byte", 1, "writeByte", "readUnsignedByte"));
        types.put("unsigned short", new PrimitiveTypeInfo("int", "Integer", "short", 2, "writeShort", "readUnsignedShort"));
        types.put("unsigned int", new PrimitiveTypeInfo("long", "Long", "int", 4, "writeInt", "readInt"));
        types.put("byte", new PrimitiveTypeInfo("byte", "Byte", "byte", 1, "writeByte", "readByte"));
        types.put("short", new PrimitiveTypeInfo("short", "Short", "short", 2, "writeShort", "readShort"));
        types.put("int", new PrimitiveTypeInfo("int", "Integer", "int", 4, "writeInt", "readInt"));
        types.put("long", new PrimitiveTypeInfo("long", "Long", "long", 8, "writeLong", "readLong"));
        types.put("double", new PrimitiveTypeInfo("double", "Double", "double", 8, "writeDouble", "readDouble"));
        types.put("float", new PrimitiveTypeInfo("float", "Float", "float", 4, "writeFloat", "readFloat"));
        writers.put(ClassAttribute.ClassAttributeType.PADDING, new JavaPaddingGenerator());
        writers.put(ClassAttribute.ClassAttributeType.PRIMITIVE, new JavaPrimitiveGenerator());
        writers.put(ClassAttribute.ClassAttributeType.CLASSREF, new JavaClassRefGenerator());
        writers.put(ClassAttribute.ClassAttributeType.ENUMREF, new JavaEnumRefGenerator());
        writers.put(ClassAttribute.ClassAttributeType.FIXED_LIST, new JavaFixedLengthListGenerator());
        writers.put(ClassAttribute.ClassAttributeType.VARIABLE_LIST, new JavaVariableLengthListGenerator());
        writers.put(ClassAttribute.ClassAttributeType.BITFIELD, new JavaBitfieldGenerator());
        writers.put(ClassAttribute.ClassAttributeType.BOOLEAN, new JavaBooleanGenerator());
        writers.put(ClassAttribute.ClassAttributeType.UNSET, null);
    }
