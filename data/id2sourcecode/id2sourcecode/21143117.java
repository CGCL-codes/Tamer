    public void testAttributeChange() throws Exception {
        ClientFacade facade = (ClientFacade) getContext().lookup(ClientFacade.ROLE + "/sql-facade");
        facade.login("admin", "".toCharArray());
        changeEventType(facade);
        facade.logout();
        CachableStorageOperator operator = (CachableStorageOperator) getContext().lookup(CachableStorageOperator.ROLE + "/sql");
        operator.disconnect();
        testTypeIds();
        operator.connect();
        changeEventType(facade);
        testTypeIds();
    }
