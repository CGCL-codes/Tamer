    public StringWriter toStringWriter(String source) {
        StringWriter writer = new StringWriter();
        Scanner scanner;
        scanner = new Scanner(source);
        while (scanner.hasNextLine()) {
            writer.append(scanner.nextLine()).append(Constants.FS);
        }
        return writer;
    }
