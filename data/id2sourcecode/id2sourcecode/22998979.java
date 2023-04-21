    public static void appendOneFileToAnother(File baseFile, File fileToAppend) throws IOException {
        FileWriter writer = new FileWriter(baseFile, true);
        Scanner scanner = new Scanner(fileToAppend);
        try {
            while (scanner.hasNextLine()) {
                writer.append(scanner.nextLine()).append("\n");
            }
        } finally {
            scanner.close();
            writer.close();
        }
    }
