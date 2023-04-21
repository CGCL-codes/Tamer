    public void writeInput(File inputFile) {
        BufferedReader input;
        try {
            input = new BufferedReader(new FileReader(inputFile));
            String nextLine = input.readLine();
            while (nextLine != null) {
                println(nextLine);
                nextLine = input.readLine();
            }
            input.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
