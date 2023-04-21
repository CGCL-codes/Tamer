    @TestTargetNew(level = TestLevel.COMPLETE, notes = "Verifies newPermissionCollection() method.", method = "newPermissionCollection", args = {  })
    public void test_newPermissionCollection() {
        char s = File.separatorChar;
        FilePermission perm[] = new FilePermission[4];
        perm[0] = readAllFiles;
        perm[1] = allInCurrent;
        perm[2] = new FilePermission(s + "tmp" + s + "test" + s + "*", "read,write");
        perm[3] = new FilePermission(s + "tmp" + s + "test" + s + "collection.file", "read");
        PermissionCollection collect = perm[0].newPermissionCollection();
        for (int i = 0; i < perm.length; i++) {
            collect.add(perm[i]);
        }
        assertTrue("returned false for subset of files", collect.implies(new FilePermission("*", "write")));
        assertTrue("returned false for subset of name and action", collect.implies(new FilePermission(s + "tmp", "read")));
        assertTrue("returned true for non subset of file and action", collect.implies(readInFile));
        FilePermission fp1 = new FilePermission("/tmp/-".replace('/', File.separatorChar), "read");
        PermissionCollection fpc = fp1.newPermissionCollection();
        fpc.add(fp1);
        fpc.add(new FilePermission("/tmp/scratch/foo/*".replace('/', File.separatorChar), "write"));
        FilePermission fp2 = new FilePermission("/tmp/scratch/foo/file".replace('/', File.separatorChar), "read,write");
        assertTrue("collection does not collate", fpc.implies(fp2));
    }
