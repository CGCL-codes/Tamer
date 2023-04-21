    protected IParser createAttribute_2003Parser() {
        EAttribute[] features = new EAttribute[] { ModelPackage.eINSTANCE.getAttribute_Name(), ModelPackage.eINSTANCE.getAttribute_Datatype(), ModelPackage.eINSTANCE.getAttribute_PrimaryKey() };
        MessageFormatParser reader = new MessageFormatParser(features);
        reader.setViewPattern("{0} : {1} {2,choice,0#|1#'<<'id'>>'}");
        reader.setEditorPattern("{0} : {1}");
        RegexpParser writer = new RegexpParser(features);
        writer.setEditPattern("{0} *: *{1}");
        return new CompositeParser(reader, writer);
    }
