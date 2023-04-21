    public void init() throws Exception {
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        builder.setErrorHandler(errorHandler);
        URL urlFsmlUrl = null;
        try {
            urlFsmlUrl = new URL(fsmlUrl);
            dom = builder.parse(urlFsmlUrl.openStream());
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
            throw new Exception("Malformed URL " + fsmlUrl + ".");
        } catch (IOException ioe) {
            ioe.printStackTrace();
            throw new Exception("Could not open stream to URL " + urlFsmlUrl.toExternalForm() + ".");
        } catch (SAXParseException err) {
            throw new Exception("** Parsing error" + ", line " + err.getLineNumber() + ", uri " + err.getSystemId() + "   " + err.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
        if (errorHandler.getNumErrors() > 0) {
            throw new FsmlException("Errors ocurred while parsing FSML");
        }
        root = dom.getDocumentElement();
        data = (Element) root.getElementsByTagName("FileData").item(0);
        structure = (Element) root.getElementsByTagName("FileStructure").item(0);
    }
