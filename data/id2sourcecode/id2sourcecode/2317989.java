    public void testCategoryAnnotation() throws RaplaException {
        String sampleDoc = "This is the category for user-groups";
        String sampleAnnotationValue = "documentation";
        facade.login("homer", "duffs".toCharArray());
        {
            Category userGroups = (Category) facade.edit(facade.getUserGroupsCategory());
            userGroups.setAnnotation(sampleAnnotationValue, sampleDoc);
            facade.store(userGroups);
        }
        operator.disconnect();
        operator.connect();
        facade.login("homer", "duffs".toCharArray());
        {
            Category userGroups = facade.getUserGroupsCategory();
            assertEquals(sampleDoc, userGroups.getAnnotation(sampleAnnotationValue));
        }
    }
