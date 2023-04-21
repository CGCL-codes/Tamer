    @Test
    public void adminAuthorizeFunctionalTest_grantReadAndWrite() throws Exception {
        System.out.println("adminAuthorizeFunctionalTest_grantReadAndWrite -- client is granted authorization to read and write a property");
        Authorize request = new Authorize();
        request.setItem(createItem(ITEM_1));
        Property prop = new Property();
        prop.setName(PROP_1);
        Partner partner = new Partner();
        partner.setString(CLIENT_NAME);
        partner.setAction(Partner.Action.GRANT);
        partner.setValue(Partner.Value.WRITE);
        prop.addPartner(partner);
        partner = new Partner();
        partner.setString(CLIENT_NAME);
        partner.setAction(Partner.Action.GRANT);
        partner.setValue(Partner.Value.READ);
        prop.addPartner(partner);
        request.addProperty(prop);
        mgmtAdmin.authorize(request);
        CreateEvent evt = createCreateEvent(ITEM_1, getPastDate(HOUR), createPropertyValueUpdate(PROP_1));
        basicClient.createEvent(evt);
        GetPropertyValues gpva = new GetPropertyValues();
        gpva.addProperty(PROP_1);
        gpva.setItem(createItem(ITEM_1));
        PropertyValuesReport pvr = basicClient.getPropertyValues(gpva);
    }
