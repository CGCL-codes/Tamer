    public void testGetCollection() throws Exception {
        Class cl = Class.forName(DB_DRIVER);
        Database database = (Database) cl.newInstance();
        DatabaseManager.registerDatabase(database);
        Collection rootCollection = DatabaseManager.getCollection(URI + "/db", "admin", null);
        CollectionManagementService cms = (CollectionManagementService) rootCollection.getService("CollectionManagementService", "1.0");
        Collection adminCollection = cms.createCollection(ADMIN_COLLECTION_NAME);
        UserManagementService ums = (UserManagementService) rootCollection.getService("UserManagementService", "1.0");
        if (ums != null) {
            Permission p = new Permission();
            p.setPermissions("user=+read,+write,group=-read,-write,other=-read,-write");
            ums.setPermissions(adminCollection, p);
            Collection guestCollection = DatabaseManager.getCollection(URI + "/db/" + ADMIN_COLLECTION_NAME, "guest", "guest");
            Resource resource = guestCollection.createResource("testguest", "BinaryResource");
            resource.setContent("123".getBytes());
            try {
                guestCollection.storeResource(resource);
                fail();
            } catch (XMLDBException e) {
            }
            cms.removeCollection(ADMIN_COLLECTION_NAME);
        }
    }
