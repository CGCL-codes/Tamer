    public void testCategoryChange() throws RaplaException {
        facade.login("homer", "duffs".toCharArray());
        {
            Category category1 = facade.newCategory();
            Category category2 = facade.newCategory();
            category1.setKey("users1");
            category2.setKey("users2");
            Category groups = (Category) facade.edit(facade.getUserGroupsCategory());
            groups.addCategory(category1);
            groups.addCategory(category2);
            facade.store(groups);
            assertEquals("users1", facade.getUserGroupsCategory().getCategories()[3].getKey());
            assertEquals("users2", facade.getUserGroupsCategory().getCategories()[4].getKey());
            operator.disconnect();
            operator.connect();
            facade.refresh();
        }
        assertEquals("users1", facade.getUserGroupsCategory().getCategories()[3].getKey());
        assertEquals("users2", facade.getUserGroupsCategory().getCategories()[4].getKey());
    }
