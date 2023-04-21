    private void readFile(String filePath) {
        try {
            CSVReader reader = new CSVReader(new FileReader(filePath));
            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                if (!nextLine[1].equals("") && !nextLine[1].equals("FirstName")) {
                    Debug.print("Importing: " + nextLine[0] + " " + nextLine[1]);
                }
            }
        } catch (Exception e) {
            Debug.print(e.getMessage());
        }
    }
