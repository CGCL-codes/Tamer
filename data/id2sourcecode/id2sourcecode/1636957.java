    private boolean performSendMapping(IFile file, WSMXRepository _instance) throws Exception {
        InputStream inStr = file.getContents();
        byte[] buffer = new byte[inStr.available()];
        inStr.read(buffer);
        inStr.close();
        String fileContent = new String(buffer, "UTF-8");
        final DocumentBuilderFactory FAC = DocumentBuilderFactory.newInstance();
        FAC.setValidating(false);
        FAC.setNamespaceAware(false);
        DocumentBuilder docBuilder = FAC.newDocumentBuilder();
        inStr = file.getContents();
        Document doc = docBuilder.parse(inStr);
        inStr.close();
        NodeList nl = doc.getElementsByTagName("dc:identifier");
        if (nl.getLength() == 0) {
            throw new Exception("Invalid mapping file - no ID detected");
        }
        String mappingID = ((Element) nl.item(0)).getAttribute("rdf:resource");
        boolean existsMapping = false;
        for (MappingDocument mDoc : _instance.retrieveMappings()) {
            if (mappingID.equals(mDoc.getId().toString())) {
                existsMapping = true;
                break;
            }
        }
        if (existsMapping == true && false == MessageDialog.openConfirm(Display.getCurrent().getActiveShell(), "Mapping Overwrite?", "Mapping file with id '" + mappingID + "' already exists on the remote repository.\n" + "\nPlease confirm overwriting ?")) {
            return false;
        }
        _instance.storeMapping(fileContent);
        return true;
    }
