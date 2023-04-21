    @Test
    public void should_list_files() throws IOException {
        File dir = new File("res/dir/test");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        assertTrue(dir.isDirectory());
        File file = new File("res/dir/test.file");
        if (!file.exists()) {
            file.createNewFile();
        }
        assertTrue(file.isFile());
        File file2 = new File("res/dir/test.file2");
        if (!file2.exists()) {
            file2.createNewFile();
        }
        assertTrue(file2.isFile());
        File parentDir = dir.getParentFile();
        assertEquals(3, parentDir.list().length);
        assertEquals(3, parentDir.listFiles().length);
        FilenameFilter filenameFilter = new FilenameFilter() {

            public boolean accept(File dir, String name) {
                return name.endsWith("file");
            }
        };
        assertEquals(1, parentDir.list(filenameFilter).length);
        dir.delete();
        file.delete();
        file2.delete();
        parentDir.delete();
    }
