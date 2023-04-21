    public void testSaveFile() throws IOException {
        File writeTo = File.createTempFile("fileops", ".test").getCanonicalFile();
        writeTo.deleteOnExit();
        File backup = new File(writeTo.getPath() + "~");
        FileOps.saveFile(new FileOps.DefaultFileSaver(writeTo) {

            public void saveTo(OutputStream os) throws IOException {
                String output = "version 1";
                os.write(output.getBytes());
            }

            public boolean shouldBackup() {
                return false;
            }
        });
        assertEquals("save w/o backup", "version 1", FileOps.readFileAsString(writeTo));
        assertEquals("save w/o backup did not backup", false, backup.exists());
        FileOps.saveFile(new FileOps.DefaultFileSaver(writeTo) {

            public void saveTo(OutputStream os) throws IOException {
                String output = "version 2";
                os.write(output.getBytes());
            }
        });
        assertEquals("save2 w backup", "version 2", FileOps.readFileAsString(writeTo));
        assertEquals("save2 w backup did backup", "version 1", FileOps.readFileAsString(backup));
        FileOps.saveFile(new FileOps.DefaultFileSaver(writeTo) {

            public void saveTo(OutputStream os) throws IOException {
                String output = "version 3";
                os.write(output.getBytes());
            }
        });
        assertEquals("save3 w backup on", "version 3", FileOps.readFileAsString(writeTo));
        assertEquals("save3 w backup on did not backup", "version 1", FileOps.readFileAsString(backup));
        try {
            FileOps.saveFile(new FileOps.DefaultFileSaver(writeTo) {

                public void saveTo(OutputStream os) throws IOException {
                    String output = "version 4";
                    os.write(output.getBytes());
                    throw new IOException();
                }
            });
            fail("IOException not propagated");
        } catch (IOException ioe) {
        }
        assertEquals("failed save4 w/o backup", "version 3", FileOps.readFileAsString(writeTo));
        assertEquals("failed save4 w/o backup check original backup", "version 1", FileOps.readFileAsString(backup));
        try {
            FileOps.saveFile(new FileOps.DefaultFileSaver(writeTo) {

                public boolean shouldBackup() {
                    return true;
                }

                public void saveTo(OutputStream os) throws IOException {
                    String output = "version 5";
                    os.write(output.getBytes());
                    throw new IOException();
                }
            });
            fail("IOException not propagated spot 2");
        } catch (IOException ioe) {
        }
        assertEquals("failed save5 w backup", "version 3", FileOps.readFileAsString(writeTo));
        try {
            FileOps.readFileAsString(backup);
            fail("The backup file should no longer exist.");
        } catch (FileNotFoundException e) {
        }
        writeTo.setReadOnly();
        try {
            FileOps.saveFile(new FileOps.DefaultFileSaver(writeTo) {

                public boolean shouldBackup() {
                    return true;
                }

                public void saveTo(OutputStream os) throws IOException {
                    String output = "version 6";
                    os.write(output.getBytes());
                }
            });
            fail("The file to be saved was read-only!");
        } catch (IOException ioe) {
        }
        assertEquals("failed save6 w backup", "version 3", FileOps.readFileAsString(writeTo));
        try {
            FileOps.readFileAsString(backup);
            fail("The backup file should no longer exist.");
        } catch (FileNotFoundException e) {
        }
    }
