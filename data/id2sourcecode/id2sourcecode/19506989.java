    private void writeSeparatedInputXml(String pathForXml, String xmlFileName, Document xmlDocument, Writer writer, String packageName) throws InputXMLAlreadyExistsException, IOException {
        pathForXml = pathForXml.substring(0, pathForXml.lastIndexOf(PATHSEP));
        if (xmlFileName.indexOf(".xml") == -1) xmlFileName += ".xml";
        File newXmlFile = new File(pathForXml + PATHSEP + xmlFileName);
        if (newXmlFile.exists() == true) {
            if (actionOnInputXmlExistence.equals("fail")) {
                throw new InputXMLAlreadyExistsException("File " + newXmlFile.getAbsolutePath() + " does not exist.", newXmlFile.getAbsolutePath());
            } else if (actionOnInputXmlExistence.equals("override")) {
                createXMLFile(newXmlFile, xmlDocument);
            } else if (actionOnInputXmlExistence.equals("noOverride")) {
            }
        } else {
            createXMLFile(newXmlFile, xmlDocument);
        }
        writer.write("\t\tInputStream is =  ClassLoader.getSystemResourceAsStream(\"" + packageName.replace('.', '/') + "/" + xmlFileName + "\");");
        writer.write("\n\t\tDocument inDoc = makeDocument(is);\n");
    }
