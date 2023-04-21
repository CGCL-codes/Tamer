    public void testNewAttribute() throws Exception {
        ClientFacade facade = (ClientFacade) getContext().lookup(ClientFacade.ROLE + "/sql-facade");
        facade.login("admin", "".toCharArray());
        DynamicType roomType = (DynamicType) facade.edit(facade.getDynamicType("room"));
        Attribute attribute = facade.newAttribute(AttributeType.STRING);
        attribute.setKey("color");
        attribute.setAnnotation(AttributeAnnotations.KEY_EDIT_VIEW, AttributeAnnotations.VALUE_NO_VIEW);
        roomType.addAttribute(attribute);
        facade.store(roomType);
        roomType = (DynamicType) facade.getPersistant(roomType);
        Allocatable[] allocatables = facade.getAllocatables(new ClassificationFilter[] { roomType.newClassificationFilter() });
        Allocatable allocatable = (Allocatable) facade.edit(allocatables[0]);
        allocatable.getClassification().setValue("color", "665532");
        String name = (String) allocatable.getClassification().getValue("name");
        facade.store(allocatable);
        facade.logout();
        CachableStorageOperator operator = (CachableStorageOperator) getContext().lookup(CachableStorageOperator.ROLE + "/sql");
        operator.disconnect();
        operator.connect();
        facade.login("admin", "".toCharArray());
        allocatables = facade.getAllocatables(new ClassificationFilter[] { roomType.newClassificationFilter() });
        allocatable = (Allocatable) facade.edit(allocatables[0]);
        assertEquals(name, allocatable.getClassification().getValue("name"));
    }
