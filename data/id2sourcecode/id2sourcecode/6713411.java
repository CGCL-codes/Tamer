    public static void main(String args[]) throws Exception {
        if (args.length != 2) {
            System.out.println("Usage: StemTermList <in-term-list> <out-term>");
            System.exit(1);
        }
        Stemmer stemmer = new EnglishStemmer();
        PrintWriter writer = new PrintWriter(args[1]);
        BufferedReader br = new BufferedReader(new FileReader(args[0]));
        for (String line = null; (line = br.readLine()) != null; ) writer.println(stemmer.stem(line.trim()));
        writer.close();
    }
