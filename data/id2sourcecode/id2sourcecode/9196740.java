    public void testMergeAllFiles2() {
        List<String> listOfFilename = new ArrayList<String>();
        listOfFilename.add(inputDirectory + "doc_with_tab_cr_as_text.docx");
        listOfFilename.add(inputDirectory + "pure_table.docx");
        listOfFilename.add(inputDirectory + "different_paragraph_format.docx");
        listOfFilename.add(inputDirectory + "read_only_inactivated_in_xml.docx");
        listOfFilename.add(inputDirectory + "read_only_with_write_parts.docx");
        listOfFilename.add(inputDirectory + "chapter_2.docx");
        String resultFilename = "resultMultipleMerge2.docx";
        String expectedResult = expectedDirectory + resultFilename;
        File destFile = new File(outputDirectory + resultFilename);
        try {
            assertTrue(WordDocument.mergeAllFiles(destFile, listOfFilename, MergeStyle.KEEP_DOC_AS_IS));
            OpenXmlAssert.assertEquals(new File(outputDirectory + resultFilename), new File(expectedResult));
        } catch (Exception e) {
            logger.error(e);
            fail(e.getMessage());
        }
    }
