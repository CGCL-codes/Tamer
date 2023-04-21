    private void writeDatatype(DatatypeDef theFieldDef, Set<String> theAlreadyWrittenDatatypes, String version) throws Exception {
        if (theAlreadyWrittenDatatypes != null) {
            if (theAlreadyWrittenDatatypes.contains(theFieldDef.getType())) {
                return;
            } else {
                theAlreadyWrittenDatatypes.add(theFieldDef.getType());
            }
        }
        String parent = myTargetDirectory + "datatype/";
        FileUtils.forceMkdir(new File(parent));
        String fileName = parent + theFieldDef.getType() + "." + myFileExt;
        DataTypeGenerator.writeDatatype(fileName, version, theFieldDef, myBasePackage, myTemplatePackage);
        for (DatatypeDef next : theFieldDef.getSubComponentDefs()) {
            writeDatatype(next, theAlreadyWrittenDatatypes, version);
        }
    }
