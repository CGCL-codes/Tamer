    public static void main(String[] args) throws IOException {
        CSVReader reader = new CSVReader(new FileReader(ADDRESS_FILE));
        String[] nextLine;
        while ((nextLine = reader.readNext()) != null) {
            int numLines = nextLine.length;
            System.out.println("Number of Data Items: " + numLines);
            for (int i = 0; i < numLines; i++) {
                System.out.println("     nextLine[" + i + "]:  " + nextLine[i]);
            }
        }
    }
