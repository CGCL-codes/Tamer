    @Test
    public void testMarcPrinter() {
        System.setProperty("org.marc4j.marc.MarcFactory", "org.solrmarc.marcoverride.NoSortMarcFactoryImpl");
        String testDataParentPath = System.getProperty("test.data.path");
        String testConfigFile = System.getProperty("test.config.file");
        if (testDataParentPath == null) fail("property test.data.path must be defined for the tests to run");
        if (testConfigFile == null) fail("property test.config.file be defined for this test to run");
        ByteArrayOutputStream out1 = new ByteArrayOutputStream();
        CommandLineUtils.runCommandLineUtil("org.solrmarc.marc.MarcPrinter", "main", null, out1, new String[] { testConfigFile, testDataParentPath + "/u4.mrc", "to_xml" });
        CommandLineUtils.compareUtilOutputLine(new ByteArrayInputStream(out1.toByteArray()), "    <leader>01218cam a2200313 a 4500</leader>", 2);
        CommandLineUtils.compareUtilOutputLine(new ByteArrayInputStream(out1.toByteArray()), "      <subfield code=\"a\">The princes of Hà-tiên (1682-1867) /</subfield>", 39);
        ByteArrayInputStream in2 = new ByteArrayInputStream(out1.toByteArray());
        ByteArrayOutputStream out2 = new ByteArrayOutputStream();
        CommandLineUtils.runCommandLineUtil("org.solrmarc.marc.MarcPrinter", "main", in2, out2, new String[] { testConfigFile, "translate" });
        ByteArrayInputStream in3 = new ByteArrayInputStream(out2.toByteArray());
        ByteArrayOutputStream out3 = new ByteArrayOutputStream();
        CommandLineUtils.runCommandLineUtil("org.solrmarc.marc.MarcPrinter", "main", in3, out3, new String[] { testConfigFile, "print" });
        CommandLineUtils.compareUtilOutputLine(new ByteArrayInputStream(out3.toByteArray()), "LEADER 01222cam a2200313 a 4500", 0);
        CommandLineUtils.compareUtilOutputLine(new ByteArrayInputStream(out3.toByteArray()), "245 14$aThe princes of Hà-tiên (1682-1867) /$cNicholas Sellers.", 13);
        ByteArrayInputStream in4 = new ByteArrayInputStream(out2.toByteArray());
        ByteArrayOutputStream out4 = new ByteArrayOutputStream();
        CommandLineUtils.runCommandLineUtil("org.solrmarc.marc.MarcPrinter", "main", in4, out4, new String[] { testConfigFile, "untranslate" });
        File origfile = new File(testDataParentPath + "/u4.mrc");
        ByteArrayOutputStream outOrig = null;
        try {
            BufferedInputStream origFileStream = new BufferedInputStream(new FileInputStream(origfile));
            outOrig = new ByteArrayOutputStream();
            int byteread;
            while ((byteread = origFileStream.read()) != -1) {
                outOrig.write(byteread);
            }
        } catch (FileNotFoundException e) {
            fail("Error opening file: " + testDataParentPath + "/u4.mrc");
        } catch (IOException e) {
            fail("Error reading from file: " + testDataParentPath + "/u4.mrc");
        }
        CommandLineUtils.assertArrayEquals("original MARC8 record, and roundtripped MARC8 record ", out4.toByteArray(), outOrig.toByteArray());
        ByteArrayInputStream in6 = new ByteArrayInputStream(out4.toByteArray());
        ByteArrayOutputStream out6 = new ByteArrayOutputStream();
        CommandLineUtils.runCommandLineUtil("org.solrmarc.marc.MarcPrinter", "main", in6, out6, new String[] { testConfigFile, "index", "title_display" });
        CommandLineUtils.compareUtilOutputLine(new ByteArrayInputStream(out6.toByteArray()), "u4 : title_display = The princes of Hà-tiên (1682-1867)", 0);
        System.out.println("Test testMarcPrinter is successful");
    }
