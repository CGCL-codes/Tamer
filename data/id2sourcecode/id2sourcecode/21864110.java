    public String getReadCode(Type t, String ref, String id, String name, String reader) {
        switch(t) {
            case VARUINT:
            case BOOL:
            case DOUBLE:
            case SINT_8:
            case SINT_16:
            case SINT_32:
            case SINT_64:
            case UINT_8:
            case UINT_16:
            case UINT_32:
            case SSTRING:
            case BYTES:
            case STRING:
            case UINT_64:
                return name + "=" + reader + ".read" + name2writeReadNameSuffix(t) + "();";
            case ENUM:
                {
                    EnumTypeWrapper etw = protocol_wrapper.findEnumWrapper(ref);
                    StringBuffer sb = new StringBuffer(getTypeName(etw.getEnum_type().getAsType()) + " " + getReadCode(etw.getEnum_type().getAsType(), null, null, "tmp_" + name, reader) + "\n");
                    sb.append(name + "=" + etw.getClassName() + ".createEnumByValue(tmp_" + name + ");");
                    return sb.toString();
                }
            case ENUM_MAP:
                {
                    EnumTypeWrapper etw = protocol_wrapper.findEnumWrapper(ref);
                    StringBuffer sb = new StringBuffer(getTypeName(etw.getEnum_type().getAsType()) + " " + getReadCode(etw.getEnum_type().getAsType(), null, null, "tmp_" + name, reader) + "\n");
                    sb.append(name + "=" + etw.getClassName() + ".createEnumMapByValue(tmp_" + name + ");");
                    return sb.toString();
                }
            case PACKAGE:
                {
                    PackageTypeWrapper ptw = protocol_wrapper.findPackageWrapper(ref, id);
                    StringBuffer sb = new StringBuffer();
                    sb.append(name + "=new " + ptw.getClassName() + "();\n");
                    sb.append(name + ".deserializeContent(" + reader + ");");
                    return sb.toString();
                }
            case PACKAGE_MAP:
                {
                    PackageGroupTypeWrapper pgtw = protocol_wrapper.findGroupByName(ref);
                    StringBuffer sb = new StringBuffer();
                    sb.append(name + "=" + pgtw.getProtocol().getLangMetadataMap("java").get("packageName") + "." + getJavaPackageNameSuffixForGroup(pgtw) + "." + pgtw.getFactoryClassName() + ".getInstance().createPackage(" + id + ");\n");
                    sb.append(name + ".deserializeContent(" + reader + ");");
                    return sb.toString();
                }
            default:
                return "UNSUPPORTED";
        }
    }
