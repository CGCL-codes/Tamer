    public IRuleSet getRuleSetByActivityID(String activityID, OntModel ontologyModel) throws SWRLXParseException, IOException, OntologyManagerException {
        URL url2 = new URL(this.getRuleSetURL(ontologyModel, activityID));
        InputStream stream = url2.openStream();
        return this.getRuleSetByInputStream(stream);
    }
