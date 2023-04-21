    private JavaFileObject getFileObject(String antlrOutput, String grammarName, String type) throws FileNotFoundException {
        StringWriter writer = new StringWriter();
        String className = grammarName + type;
        Scanner scanner = new Scanner(new File(antlrOutput, String.format("%s.java", className)));
        while (scanner.hasNextLine()) {
            writer.append(scanner.nextLine()).append(Constants.LS);
        }
        return new SourceObject(className, writer.toString());
    }
