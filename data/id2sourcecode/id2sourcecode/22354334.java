    protected IParser createVersionAttribute_2002Parser() {
        EAttribute[] features = new EAttribute[] { ModelPackage.eINSTANCE.getVersionAttribute_Name(), ModelPackage.eINSTANCE.getVersionAttribute_Datatype() };
        MessageFormatParser reader = new MessageFormatParser(features);
        reader.setViewPattern("{0} : {1} <<version>>");
        reader.setEditorPattern("{0} : {1}");
        RegexpParser writer = new RegexpParser(features);
        writer.setEditPattern("{0} *: *{1}");
        return new CompositeParser(reader, writer);
    }
