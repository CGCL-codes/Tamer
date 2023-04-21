    private FileSetJavaFilesOnly unmarshal(Writer writer) {
        FileSetJavaFilesOnly result = null;
        Reader reader = new StringReader(writer.toString());
        try {
            result = (FileSetJavaFilesOnly) unmarshaller.unmarshal(reader);
        } catch (JAXBException e) {
            fail("failure unmarshalling " + FileSetJavaFilesOnly.class.getName() + "object");
        }
        return result;
    }
