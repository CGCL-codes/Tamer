    public void testUserStore() throws RaplaException {
        facade.login("homer", "duffs".toCharArray());
        {
            User u = facade.newUser();
            u.setUsername("kohlhaas");
            u.setAdmin(false);
            u.addGroup(facade.getUserGroupsCategory().getCategory("my-group"));
            facade.store(u);
        }
        operator.disconnect();
        operator.connect();
        facade.login("homer", "duffs".toCharArray());
        {
            User u = facade.getUser("kohlhaas");
            Category[] groups = u.getGroups();
            assertEquals(groups.length, 2);
            assertEquals(facade.getUserGroupsCategory().getCategory("my-group"), groups[1]);
            assertFalse(u.isAdmin());
        }
    }
