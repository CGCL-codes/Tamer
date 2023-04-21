    @Test
    public void testRetrieveChannelName() throws JCouplingException {
        logger.debug("Retrieving the property ...");
        Property property = null;
        Iterator<Property> PropertyIterator = (dMapper.retrieveProperty("TestChannel1")).iterator();
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
