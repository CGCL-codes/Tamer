    public void test_impliesLjava_security_Permission() {
        char s = File.separatorChar;
        FilePermission perm[] = new FilePermission[7];
        perm[0] = new FilePermission("test1.file", "write");
        perm[1] = allInCurrent;
        perm[2] = new FilePermission(s + "tmp" + s + "test" + s + "*", "read,write");
        perm[3] = new FilePermission(s + "tmp" + s + "test" + s + "collection.file", "read");
        perm[4] = new FilePermission(s + "windows" + "*", "delete");
        perm[5] = readInFile;
        perm[6] = new FilePermission("hello.file", "write");
        Permissions perms = new Permissions();
        for (int i = 0; i < perm.length; i++) {
            perms.add(perm[i]);
        }
        assertTrue("Returned true for non-subset of files", !perms.implies(new FilePermission("<<ALL FILES>>", "execute")));
        assertTrue("Returned true for non-subset of action", !perms.implies(new FilePermission(s + "tmp" + s + "test" + s + "*", "execute")));
        assertTrue("Returned false for subset of actions", perms.implies(new FilePermission("*", "write")));
        assertTrue("Returned false for subset of files", perms.implies(new FilePermission(s + "tmp" + s + "test" + s + "test.file", "read")));
        assertTrue("Returned false for subset of files and actions", perms.implies(new FilePermission(s + "tmp" + s + "test" + s + "test2.file", "write")));
    }
