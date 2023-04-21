    public void testDigest() {
        JoinedDigesterBean bean = new JoinedDigesterBean();
        Digester d = bean.buildDigester();
        URL file = TestJoinedDigesterBean.class.getResource(TEST_FILE);
        if (file == null) {
            throw new IllegalArgumentException("Error getting Test join xml file");
        }
        try {
            bean.digest(d, file);
        } catch (Exception ex) {
            ex.printStackTrace();
            fail("Error digesting data");
        }
        runDataIntegrityTest(bean);
    }
