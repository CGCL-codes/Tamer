    @Test
    public void adminAuthorizeFunctionalTest_revokeReadAndWrite() throws Exception {
        System.out.println("adminAuthorizeFunctionalTest_revokeReadAndWrite -- client is deprived of authorization to read and write a property");
        Authorize request = new Authorize();
        request.setItem(createItem(ITEM_1));
        Property prop = new Property();
        prop.setName(PROP_1);
        Partner partner = new Partner();
        partner.setString(CLIENT_NAME);
        partner.setAction(Partner.Action.REVOKE);
        partner.setValue(Partner.Value.WRITE);
        prop.addPartner(partner);
        partner = new Partner();
        partner.setString(CLIENT_NAME);
        partner.setAction(Partner.Action.REVOKE);
        partner.setValue(Partner.Value.READ);
        prop.addPartner(partner);
        request.addProperty(prop);
        mgmtAdmin.authorize(request);
        try {
            CreateEvent evt = createCreateEvent(ITEM_1, getPastDate(HOUR), createPropertyValueUpdate(PROP_1));
            basicClient.createEvent(evt);
            fail("The service should have thrown an authorization error exception.");
        } catch (AuthorizationFault ex) {
        }
        GetPropertyValues gpva = new GetPropertyValues();
        gpva.addProperty(PROP_1);
        gpva.setItem(createItem(ITEM_1));
        PropertyValuesReport pvr = basicClient.getPropertyValues(gpva);
        if (pvr.getProperty(0).sizeValue() > 0 || pvr.getProperty(0).getCanRead()) {
            fail("The service should have thrown an authorization error exception.");
        }
    }
