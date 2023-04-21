    public double getSemanticSimilarity(String a, String b) {
        URL url;
        URLConnection con = null;
        String str = "";
        String output = "";
        DataInputStream input;
        Vector<String> synonyms = new Vector<String>();
        a = a.toLowerCase();
        b = b.toLowerCase();
        try {
            url = new URL("http://freethesaurus.net/s.php?q=" + a);
            con = url.openConnection();
            input = new DataInputStream(con.getInputStream());
            while (null != ((str = input.readLine()))) {
                output += str;
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        Scanner reader = new Scanner(output);
        String result = "";
        boolean print = false;
        while (reader.hasNext()) {
            String pick = reader.next();
            if (pick.indexOf("class=\"syn\">") >= 0) {
                print = true;
            }
            if (print && pick.indexOf("</div>") >= 0) {
                break;
            }
            if (print) {
                result += pick;
            }
        }
        Scanner parser = new Scanner(result);
        parser.useDelimiter("<spanonmouseover([^>]*)>");
        while (parser.hasNext()) {
            String str1[] = parser.next().split("<");
            if (str1[0].indexOf("class") < 0) {
                synonyms.add(str1[0].toLowerCase());
            }
        }
        if (synonyms.contains(b)) {
            System.out.println(a + " and " + b + " are synonyms ");
            return 1.0;
        }
        return 0.0;
    }
