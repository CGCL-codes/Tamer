    public static ArrayList<String> readBins(File data, NarrWriter narr) throws IOException {
        BufferedReader input;
        try {
            input = new BufferedReader(new FileReader(data));
        } catch (IOException e) {
            System.out.println("Invalid input file file won't open");
            return null;
        }
        String nextLine = input.readLine();
        if (nextLine == null) {
            System.out.println("Invalid input file first line null");
            return null;
        }
        String storage = nextLine;
        StringTokenizer a;
        String x;
        int value;
        while (nextLine != null) {
            a = new StringTokenizer(nextLine);
            a.nextToken();
            if (!a.hasMoreTokens()) {
                System.out.println("Invalid input file one line doesn't have 2 tokens");
                return null;
            }
            value = new Integer(a.nextToken()).intValue();
            if (value > 1) {
                break;
            }
            narr.println(nextLine);
            storage = nextLine;
            nextLine = input.readLine();
        }
        ArrayList<String> output = new ArrayList<String>();
        output.add(storage);
        while (nextLine != null) {
            output.add(nextLine);
            narr.println(nextLine);
            nextLine = input.readLine();
        }
        input.close();
        return output;
    }
