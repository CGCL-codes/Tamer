    private void thenAssertRepoContentIsCorrect() throws FileNotFoundException, Exception, IOException {
        FileInputStream rpmInputStream = new FileInputStream(RPM_FILE);
        extractFormat(rpmInputStream);
        InputStream uncompressed = new GZIPInputStream(rpmInputStream);
        byte[] buffer = toByteArray(uncompressed);
        String content = new String(assertRpmContainsFile(buffer, REPO_FILENAME));
        assertThat(content, containsString(BASE_URL + "/service/local/yum/" + REPO_ID + "/" + VERSION));
    }
