    private static String readInput() {
        if (userInputReader == null) {
            userInputReader = new BufferedReader(new InputStreamReader(System.in));
        }
        try {
            return userInputReader.readLine();
        } catch (IOException e) {
            consol.write("Unable to read input");
            System.exit(1);
        }
        return null;
    }
