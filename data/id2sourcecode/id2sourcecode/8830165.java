    @TestTargetNew(level = TestLevel.COMPLETE, notes = "Verifies implies(java.security.Permission) method.", method = "implies", args = { java.security.Permission.class })
    public void test_impliesLjava_security_Permission() {
        assertTrue("Returned true for non-subset of actions", !readAllFiles.implies(allInCurrent));
        assertTrue("Returned true for non-subset of files", !allInCurrent.implies(readAllFiles));
        assertTrue("Returned false for subset of actions", allInCurrent.implies(readInCurrent));
        assertTrue("Returned false for subset of files", readAllFiles.implies(readInCurrent));
        assertTrue("Returned false for subset of files and actions", allInCurrent.implies(readInFile));
        assertTrue("Returned false for equal FilePermissions", readAllFiles.implies(alsoReadAllFiles));
        FilePermission fp3 = new FilePermission("/bob/*".replace('/', File.separatorChar), "read,write");
        FilePermission fp4 = new FilePermission("/bob/".replace('/', File.separatorChar), "write");
        assertTrue("returned true for same dir using * and not *", !fp3.implies(fp4));
        FilePermission fp5 = new FilePermission("/bob/file".replace('/', File.separatorChar), "write");
        assertTrue("returned false for same dir using * and file", fp3.implies(fp5));
        FilePermission fp6 = new FilePermission("/bob/".replace('/', File.separatorChar), "read,write");
        FilePermission fp7 = new FilePermission("/bob/*".replace('/', File.separatorChar), "write");
        assertTrue("returned false for same dir using not * and *", !fp6.implies(fp7));
        assertTrue("returned false for same subdir", fp6.implies(fp4));
        FilePermission fp8 = new FilePermission("/".replace('/', File.separatorChar), "read,write");
        FilePermission fp9 = new FilePermission("/".replace('/', File.separatorChar), "write");
        assertTrue("returned false for same dir", fp8.implies(fp9));
        FilePermission fp10 = new FilePermission("/".replace('/', File.separatorChar), "read,write");
        FilePermission fp11 = new FilePermission("/".replace('/', File.separatorChar), "write");
        assertTrue("returned false for same dir", fp10.implies(fp11));
        FilePermission fp12 = new FilePermission("/*".replace('/', File.separatorChar), "read,write");
        assertTrue("returned false for same dir using * and dir", !fp12.implies(fp10));
    }
