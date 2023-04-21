    private void readFile(PrintWriter writer) throws FileNotFoundException, IOException {
        BufferedReader bReader = new BufferedReader(new FileReader(logFile));
        while (bReader.ready()) writer.println(bReader.readLine());
    }
