    @Test
    public void testStreamFlushing() throws IOException {
        String WRITE_FILE = "myfile.csv";
        String[] nextLine = new String[] { "aaaa", "bbbb", "cccc", "dddd" };
        FileWriter fileWriter = new FileWriter(WRITE_FILE);
        CSVWriter writer = new CSVWriter(fileWriter);
        writer.writeNext(nextLine);
        writer.close();
    }
