    public void testGetCollection() {
        try {
            Class<?> cl = Class.forName(DB_DRIVER);
            Database database = (Database) cl.newInstance();
            DatabaseManager.registerDatabase(database);
            Collection rootCollection = DatabaseManager.getCollection(URI + DBBroker.ROOT_COLLECTION, "admin", "");
            CollectionManagementService cms = (CollectionManagementService) rootCollection.getService("CollectionManagementService", "1.0");
            Collection adminCollection = cms.createCollection(ADMIN_COLLECTION_NAME);
            UserManagementService ums = (UserManagementService) rootCollection.getService("UserManagementService", "1.0");
            if (ums != null) {
                Permission p = ums.getPermissions(adminCollection);
                p.setMode(Permission.USER_STRING + "=+read,+write," + Permission.GROUP_STRING + "=-read,-write," + Permission.OTHER_STRING + "=-read,-write");
                ums.setPermissions(adminCollection, p);
                Collection guestCollection = DatabaseManager.getCollection(URI + DBBroker.ROOT_COLLECTION + "/" + ADMIN_COLLECTION_NAME, "guest", "guest");
                Resource resource = guestCollection.createResource("testguest", "BinaryResource");
                resource.setContent("123".getBytes());
                try {
                    guestCollection.storeResource(resource);
                    fail();
                } catch (XMLDBException e) {
                }
                cms.removeCollection(ADMIN_COLLECTION_NAME);
            }
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
