    public void testOverwriteInvisibleResource() throws Exception {
        CmsObject cms = getCmsObject();
        echo("Testing to overwrite invisible resource");
        String source = "index.html";
        String target = "/test_index.html";
        cms.createResource(target, CmsResourceTypePlain.getStaticTypeId());
        cms.chacc(target, I_CmsPrincipal.PRINCIPAL_USER, "test2", "-r+v+i");
        cms.unlockResource(target);
        storeResources(cms, source);
        storeResources(cms, target);
        cms.loginUser("test2", "test2");
        cms.getRequestContext().setCurrentProject(cms.readProject("Offline"));
        try {
            cms.readResource(target, CmsResourceFilter.ALL);
            fail("should fail to read the resource without permissions");
        } catch (CmsPermissionViolationException e) {
        }
        try {
            cms.copyResource(source, target);
            fail("should fail to overwrite a resource without a lock on the target");
        } catch (CmsLockException e) {
        }
        try {
            cms.lockResource(target);
            fail("should fail to overwrite the resource without read permissions");
        } catch (CmsPermissionViolationException e) {
        }
        try {
            cms.createResource(target, CmsResourceTypeXmlPage.getStaticTypeId());
            fail("should fail to create a resource that already exists");
        } catch (CmsLockException e) {
        }
        cms.loginUser("Admin", "admin");
        cms.getRequestContext().setCurrentProject(cms.readProject("Offline"));
        assertFilter(cms, source, OpenCmsTestResourceFilter.FILTER_EQUAL);
        assertFilter(cms, target, OpenCmsTestResourceFilter.FILTER_EQUAL);
    }
