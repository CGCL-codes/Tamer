    public static String readFile(String fileName) throws FileNotFoundException, IOException {
        BufferedReader inputFile;
        inputFile = new BufferedReader(new FileReader(fileName));
        StringWriter outputString = new StringWriter();
        while (inputFile.ready()) {
            outputString.write(inputFile.readLine());
            outputString.write("\n");
        }
        inputFile.close();
        String content = outputString.toString();
        outputString.close();
        return content;
    }
