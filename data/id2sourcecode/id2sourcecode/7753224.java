    @TestTargetNew(level = TestLevel.COMPLETE, notes = "", method = "implies", args = { java.security.Permission.class })
    public void test_impliesLjava_security_Permission() {
        PropertyPermission impliedByJavaPP = new PropertyPermission("java.home", "read");
        PropertyPermission notImpliedByJavaPP = new PropertyPermission("java.home", "read,write");
        PropertyPermission impliedByUserPP = new PropertyPermission("user.name", "read,write");
        PropertyPermission alsoImpliedByUserPP = new PropertyPermission("user.name", "write");
        assertTrue("Returned false for implied permission (subset of .*)", javaPP.implies(impliedByJavaPP));
        assertTrue("Returned true for unimplied permission", !javaPP.implies(notImpliedByJavaPP));
        assertTrue("Returned false for implied permission (equal)", userPP.implies(impliedByUserPP));
        assertTrue("Returned false for implied permission (subset of actions)", userPP.implies(alsoImpliedByUserPP));
    }
