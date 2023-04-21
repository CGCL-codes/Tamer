    public static String readFile(File theFile) throws FileNotFoundException, IOException {
        BufferedReader inputFile;
        inputFile = new BufferedReader(new FileReader(theFile));
        StringWriter outputString = new StringWriter();
        while (inputFile.ready()) {
            outputString.write(inputFile.readLine());
        }
        inputFile.close();
        String content = outputString.toString();
        outputString.close();
        return content;
    }
