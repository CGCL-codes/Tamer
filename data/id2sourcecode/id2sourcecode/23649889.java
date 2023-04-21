    private void writeLines_Core(String filePath, String charsetOrg, String charsetTmpTest, String crlf) throws IOException {
        BufferedReader rdSrc = new BufferedReader(new InputStreamReader(new FileInputStream(new File(filePath)), charsetOrg));
        List<String> readLines = new ArrayList<String>();
        while (rdSrc.ready()) {
            String lineSrc = rdSrc.readLine();
            readLines.add(lineSrc);
        }
        rdSrc.close();
        rdSrc = null;
        String tmpTestFile = System.getProperty("java.io.tmpdir") + File.separator + "FileUtilTest_TempFile_writeLines_Core_" + StrUtil.getRandomHexString(16, true) + ".txt";
        FileUtil.writeLines(new File(tmpTestFile), readLines, crlf, charsetOrg, null);
        java.util.List<String> checkLines = new ArrayList();
        FileUtil.readLines(filePath, charsetTmpTest, checkLines);
        File fTmpFile = new File(tmpTestFile);
        BufferedReader rdTest = new BufferedReader(new InputStreamReader(new FileInputStream(fTmpFile), charsetOrg));
        int lineIndex = 0;
        while (rdTest.ready()) {
            String lineSrc = new String(rdTest.readLine());
            String lineChk = checkLines.get(lineIndex++);
            assertEquals(lineSrc, lineChk);
        }
        rdTest.close();
        rdTest = null;
        fTmpFile.delete();
        if (fTmpFile.exists()) {
            fail("temp file can not deleted : " + tmpTestFile);
        }
    }
