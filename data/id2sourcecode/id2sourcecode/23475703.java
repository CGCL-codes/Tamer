    protected void testRawCsvRead(String originalCommentText) throws FileNotFoundException, IOException {
        CSVReader reader = new CSVReader(new FileReader(filePath));
        String[] nextLine = null;
        int count = 0;
        while ((nextLine = reader.readNext()) != null) {
            if (!nextLine[0].equals("field1")) {
                System.out.println("RawCsvRead Assert Error: Name is wrong.");
            }
            if (!nextLine[1].equals("3.0")) {
                System.out.println("RawCsvRead Assert Error: Value is wrong.");
            }
            if (!nextLine[2].equals("3,147.25")) {
                System.out.println("RawCsvRead Assert Error: Amount1 is wrong.");
            }
            if (!nextLine[3].equals("$3,147.26")) {
                System.out.println("RawCsvRead Assert Error: Currency is wrong.");
            }
            System.out.println("Field 4 read: " + nextLine[4]);
            if (!nextLine[4].equals(originalCommentText)) {
                System.out.println("RawCsvRead Assert Error: Comment is wrong.");
            }
            count++;
        }
        if (count != 3) {
            System.out.println("RawCsvRead Assert Error: Count of lines is wrong.");
        }
    }
