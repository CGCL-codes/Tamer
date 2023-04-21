    public void testListFilesRecursively() throws Exception {
        final File dir1 = new File(getOutdir(), "testListFilesRecursively");
        if (dir1.exists()) {
            assertTrue(IoUtility.deleteDir(dir1, true));
        }
        final File dir2 = new File(dir1, "dir2");
        final File file1 = new File(dir1, "dingo.txt");
        final File file2 = new File(dir1, "dongo.txt");
        final File file3 = new File(dir2, "bingo.txt");
        assertTrue(dir2.mkdirs());
        IoUtility.saveFile(file1, "File 1", "ISO-8859-1");
        IoUtility.saveFile(file2, "File 2", "ISO-8859-1");
        IoUtility.saveFile(file3, "File 3", "ISO-8859-1");
        final File dir3 = new File(dir1, "dir3");
        assertTrue(dir3.mkdirs());
        final File file5 = new File(dir3, "bongo.txt");
        IoUtility.saveFile(file5, "File 5", "ISO-8859-1");
        final List list = IoUtility.listFilesRecursively(dir1, new FileFilter() {

            public boolean accept(File pathname) {
                return pathname.getName().equals("bongo.txt");
            }
        });
        assertEquals(0, list.size());
        final List list1 = IoUtility.listFilesRecursively(dir1, new FileFilter() {

            public boolean accept(File pathname) {
                return pathname.isDirectory() || pathname.getName().equals("bongo.txt");
            }
        });
        assertEquals(1, list1.size());
        final List list2 = IoUtility.listFilesRecursively(dir1, new FileFilter() {

            public boolean accept(File pathname) {
                return pathname.getName().endsWith(".txt");
            }
        });
        assertEquals(2, list2.size());
        final List list3 = IoUtility.listFilesRecursively(dir1, new FileFilter() {

            public boolean accept(File pathname) {
                return pathname.isDirectory() || pathname.getName().endsWith(".txt");
            }
        });
        assertEquals(4, list3.size());
        final List list4 = IoUtility.listFilesRecursively(file5, new FileFilter() {

            public boolean accept(File pathname) {
                return false;
            }
        });
        assertEquals(1, list4.size());
        assertEquals(file5, list4.get(0));
        assertEquals(4, list3.size());
        final List list5 = IoUtility.listFilesRecursively(dir1, new FileFilter() {

            public boolean accept(File pathname) {
                return false;
            }
        });
        assertEquals(0, list5.size());
    }
