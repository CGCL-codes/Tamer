    @Test
    public void merge() throws Exception {
        AbstractBurster burster = new PdfBurster() {

            protected void executeController() throws Exception {
                scripting.setRoots(new String[] { "src/test/groovy", "src/main/external-resources/template/scripts/burst/samples" });
                ctx.settings.loadSettings("src/main/external-resources/template/config/burst/settings.xml");
                ctx.scripts.endExtractDocument = "merge-with-external-files.groovy";
                ctx.settings.setOutputFolder("./target/test-output/output/$input_document_name$/$now; format=\"yyyy.MM.dd_HH.mm.ss\"$");
                ctx.settings.setBackupFolder("./target/test-output/backup/$input_document_name$/$now; format=\"yyyy.MM.dd_HH.mm.ss\"$");
                ctx.settings.setQuarantineFolder("./target/test-output/quarantine/$input_document_name$/$now; format=\"yyyy.MM.dd_HH.mm.ss\"$");
                ctx.variables = new Variables(FilenameUtils.getName(ctx.inputDocumentFilePath), ctx.settings.getLanguage(), ctx.settings.getCountry(), ctx.settings.getNumberOfUserVariables());
            }

            ;
        };
        FileUtils.copyFileToDirectory(new File("src/main/external-resources/template/samples/Invoices-Dec.pdf"), new File("samples"));
        burster.burst(PAYSLIPS_REPORT_PATH, false);
        FileUtils.deleteQuietly(new File("samples"));
        String outputFolder = burster.getCtx().outputFolder + "/";
        assertEquals(tokens.size(), new File(outputFolder).listFiles().length);
        for (String token : tokens) {
            String path = burster.getCtx().outputFolder + "/" + token + ".pdf";
            File outputReport = new File(path);
            assertTrue(outputReport.exists());
            DocumentTester tester = new DocumentTester(path);
            tester.assertPageCountEquals(3);
            tester.assertContentContainsTextOnPage("{0018}", 1, TextSearchType.CONTAINS);
            tester.assertContentContainsTextOnPage("{0019}", 2, TextSearchType.CONTAINS);
            tester.assertContentContainsTextOnPage("{" + token + "}", 3, TextSearchType.CONTAINS);
            tester.close();
        }
    }
