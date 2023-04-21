    @Test(expected = AuthorizationFault.class)
    public void adminAuthorizeFunctionalTest_grantRead() throws Exception {
        System.out.println("adminAuthorizeFunctionalTest_grantRead -- client is given authorization to read a property, but not to write it");
        Authorize request = new Authorize();
        request.setItem(createItem(ITEM_1));
        Property prop = new Property();
        prop.setName(PROP_1);
        Partner partner = new Partner();
        partner.setString(CLIENT_NAME);
        partner.setAction(Partner.Action.GRANT);
        partner.setValue(Partner.Value.READ);
        prop.addPartner(partner);
        request.addProperty(prop);
        mgmtAdmin.authorize(request);
        GetPropertyValues gpva = new GetPropertyValues();
        gpva.addProperty(PROP_1);
        gpva.setItem(createItem(ITEM_1));
        PropertyValuesReport pvr = basicClient.getPropertyValues(gpva);
        assertTrue("The returned report does not contain a property", pvr.getProperties().size() > 0);
        assertTrue("The returned report contains too many properties", pvr.getProperties().size() < 2);
        assertEquals("The returned report does not contain the correct property", PROP_1, pvr.getProperties().get(0).getPropertyName());
        assertEquals("The returned report does not contain the correct number of property values", 1, pvr.getProperties().get(0).getValues().size());
        CreateEvent evt = createCreateEvent(ITEM_1, getPastDate(3 * HOUR), createPropertyValueUpdate(PROP_1));
        basicClient.createEvent(evt);
        fail("The service should have thrown an authorization error exception.");
    }
