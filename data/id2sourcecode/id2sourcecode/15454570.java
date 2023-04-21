    public void addProject(URL url, String baseURI) throws SimalRepositoryException {
        logger.info("Adding a project from " + url.toString());
        verifyInitialised();
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        Document originalDoc = null;
        DocumentBuilder db;
        try {
            db = dbf.newDocumentBuilder();
            originalDoc = db.parse(url.openStream());
            SimalRepositoryFactory.getProjectService().createProject(originalDoc);
        } catch (SAXException e) {
            throw new SimalRepositoryException("Unable to prepare data from " + url.toExternalForm() + " for adding to the repository", e);
        } catch (FileNotFoundException e) {
            throw new SimalRepositoryException("Unable to prepare data from " + url.toExternalForm() + " for adding to the repository", e);
        } catch (IOException e) {
            throw new SimalRepositoryException("Unable to prepare data from " + url.toExternalForm() + " for adding to the repository", e);
        } catch (ParserConfigurationException e) {
            throw new SimalRepositoryException("Unable to prepare data from " + url.toExternalForm() + " for adding to the repository", e);
        }
    }
