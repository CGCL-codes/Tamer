    @Test
    public void testCreateReport() throws IOException, SQLException {
        String[] inputLine = new String[] { "select count(*) from EMP;", "", "select 100 from sample_tab1;", "" };
        String[] expectedOutputLine = new String[] { "14", "", "100", "" };
        String inputSql = StringUtils.join(inputLine, IOUtils.LINE_SEPARATOR);
        Reader reader = new InputStreamReader(new ByteArrayInputStream(inputSql.getBytes()));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Writer writer = new OutputStreamWriter(baos);
        RecordCountUtils.createRecord(reader, writer, getConnection());
        writer.flush();
        String expected = StringUtils.join(expectedOutputLine, IOUtils.LINE_SEPARATOR);
        assertEquals(expected, baos.toString());
        IOUtils.closeQuietly(writer);
        IOUtils.closeQuietly(reader);
    }
