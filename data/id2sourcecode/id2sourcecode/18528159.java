    @Test
    public void adminAuthorizeFunctionalTest_revokeRead() throws Exception {
        System.out.println("adminAuthorizeFunctionalTest_revokeRead -- client is deprived of authorization to read a property, but it can still write it");
        Authorize request = new Authorize();
        request.setItem(createItem(ITEM_1));
        Property prop = new Property();
        prop.setName(PROP_1);
        Partner partner = new Partner();
        partner.setString(CLIENT_NAME);
        partner.setAction(Partner.Action.REVOKE);
        partner.setValue(Partner.Value.READ);
        prop.addPartner(partner);
        request.addProperty(prop);
        mgmtAdmin.authorize(request);
        try {
            CreateEvent evt = createCreateEvent(ITEM_1, getPastDate(HOUR), createPropertyValueUpdate(PROP_1));
            basicClient.createEvent(evt);
        } catch (Exception ex) {
            ex.printStackTrace();
            fail("Error, could not write the property!");
        }
        GetPropertyValues gpva = new GetPropertyValues();
        gpva.addProperty(PROP_1);
        gpva.setItem(createItem(ITEM_1));
        PropertyValuesReport pvr = basicClient.getPropertyValues(gpva);
        if (pvr.getProperty(0).sizeValue() > 0 || pvr.getProperty(0).getCanRead()) {
            fail("The service should have thrown an authorization error exception.");
        }
    }
