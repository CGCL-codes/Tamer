    public void test_write_read(int a_index, GB_StringAction a_sa) throws Exception {
        File l_file = FTools.getTempFile("java-test/sa.xml");
        l_file.delete();
        assertEquals(a_index + ".1", false, l_file.exists());
        GB_SAXmlTools.writeFile(a_sa, null, l_file);
        assertEquals(a_index + ".2", true, l_file.exists());
        GB_StringActionFile l_saFile = GB_SAXmlTools.readFile(l_file);
        GB_StringAction l_sa = l_saFile.getStringAction();
        GB_SATestTools.assertEquals(a_index + ".3", a_sa, l_sa);
    }
