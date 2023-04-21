    public static void main(String[] args) throws Exception {
        String alphabet = "i";
        char[] alphabetarray = alphabet.toCharArray();
        ArrayList<String> tags = new ArrayList<String>();
        for (char letter : alphabetarray) {
            tags.add(letter + "");
            for (int i = 1; i < 10; i++) tags.add((letter + "") + i + "");
        }
        String tagstring = "";
        for (String tag : tags) tagstring += tag;
        String url = "http://finance.yahoo.com/d/quotes.csv?s=IBM&f=" + tagstring;
        URLConnection conn = (new URL(url)).openConnection();
        conn.connect();
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String str = in.readLine();
        ArrayList<String> values = new NullReturningArrayList<String>();
        String[] split = str.split(",");
        for (String s : split) values.add(s);
        in.close();
        System.out.println(tags.size() + " -- " + values.size());
        for (int i = 0; i < tags.size(); i++) {
            System.out.println(tags.get(i) + "  --  " + values.get(i));
        }
    }
