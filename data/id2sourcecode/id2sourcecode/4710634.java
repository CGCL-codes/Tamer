    public void testIdempotency() throws Exception {
        String expectedFileName = testDir + "canonical_form_expected_6.xml";
        ByteArrayOutputStream outputBytes1, outputBytes2;
        outputBytes1 = new ByteArrayOutputStream();
        outputBytes2 = new ByteArrayOutputStream();
        compareXML(true, new FileInputStream(testDir + "canonical_form_test_6.xml"), new FileInputStream(expectedFileName), outputBytes1);
        compareXML(true, new ByteArrayInputStream(outputBytes1.toByteArray()), new FileInputStream(expectedFileName), outputBytes2);
        compareXML(true, new ByteArrayInputStream(outputBytes2.toByteArray()), new FileInputStream(expectedFileName), new ByteArrayOutputStream());
    }
