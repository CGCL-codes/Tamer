    public void testImageInstanceDelete() {
        File testFile = new File(testImgDir, "test1.jpg");
        File instanceFile = volume.getFilingFname(testFile);
        try {
            FileUtils.copyFile(testFile, instanceFile);
        } catch (IOException e) {
            fail(e.getMessage());
        }
        ImageInstance f = ImageInstance.create(volume, instanceFile, photo, ImageInstance.INSTANCE_TYPE_ORIGINAL);
        assertNotNull(f);
        f.delete();
        Connection conn = ImageDb.getConnection();
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM image_instances WHERE volume_id = '" + volume.getName() + "' AND fname = 'test1.jpg'");
            if (rs.next()) {
                fail("Found matching DB record after delete");
            }
        } catch (SQLException e) {
            fail("DB error:; " + e.getMessage());
        }
    }
