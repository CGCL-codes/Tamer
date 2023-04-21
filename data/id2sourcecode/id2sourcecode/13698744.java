    @Test
    public void testParsedLineWithInternalQuota() throws IOException {
        StringBuffer sb = new StringBuffer();
        sb.append("a,123\"4\"567,c").append("\n");
        CSVReader<String[]> c = new DefaultCSVReader(new StringReader(sb.toString()));
        String[] nextLine = c.readNext();
        assertEquals(3, nextLine.length);
        System.out.println(nextLine[1]);
        assertEquals("123\"4\"567", nextLine[1]);
    }
