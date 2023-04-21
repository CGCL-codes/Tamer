    @Test
    public void testRetrieveChannelID() throws JCouplingException {
        logger.debug("Retrieving the property ...");
        Property property = null;
        Iterator<Property> PropertyIterator = (dMapper.retrieveProperty(13)).iterator();
        while (PropertyIterator.hasNext()) {
            property = PropertyIterator.next();
            logger.debug("=================================================");
            logger.debug("PropertyID: " + property.getID());
            logger.debug("PropertyName: " + property.getName());
            logger.debug("Channel: " + property.getChannelID());
            logger.debug("Description: " + property.getDescription());
            logger.debug("XPathExpression: " + property.getXpathExpression());
            logger.debug("Resulttype: " + property.getDataType());
            logger.debug("Done");
        }
    }
