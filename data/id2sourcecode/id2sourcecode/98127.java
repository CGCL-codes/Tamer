    public static void main(String[] args) throws IOException {
        CSVReader reader = new CSVReader(new FileReader(ADDRESS_FILE));
        String[] nextLine;
        while ((nextLine = reader.readNext()) != null) {
            System.out.println("Name: [" + nextLine[0] + "]\nAddress: [" + nextLine[1] + "]\nEmail: [" + nextLine[2] + "]");
        }
        CSVReader reader2 = new CSVReader(new FileReader(ADDRESS_FILE));
        List allElements = reader2.readAll();
        StringWriter sw = new StringWriter();
        CSVWriter writer = new CSVWriter(sw);
        writer.writeAll(allElements);
        System.out.println("\n\nGenerated CSV File:\n\n");
        System.out.println(sw.toString());
    }
