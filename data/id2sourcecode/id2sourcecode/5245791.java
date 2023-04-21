    public void test_elements() {
        char s = File.separatorChar;
        FilePermission perm[] = new FilePermission[7];
        perm[0] = readAllFiles;
        perm[1] = allInCurrent;
        perm[2] = new FilePermission(s + "tmp" + s + "test" + s + "*", "read,write");
        perm[3] = new FilePermission(s + "tmp" + s + "test" + s + "collection.file", "read");
        perm[4] = alsoReadAllFiles;
        perm[5] = readInFile;
        perm[6] = new FilePermission("hello.file", "write");
        Permissions perms = new Permissions();
        for (int i = 0; i < perm.length; i++) {
            perms.add(perm[i]);
        }
        Enumeration e = perms.elements();
        FilePermission perm2[] = new FilePermission[10];
        int i = 0;
        while (e.hasMoreElements()) {
            perm2[i] = (FilePermission) e.nextElement();
            i++;
        }
        assertEquals("Permissions.elements did not return the correct " + "number of permission - called in element() test", i, perm.length);
    }
