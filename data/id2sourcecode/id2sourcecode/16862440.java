    public static void main(String[] args) throws Exception {
        strings[0] = "useful";
        strings[1] = "useless";
        strings[2] = "improve";
        strings[3] = "matter";
        strings[4] = "transxxxitional";
        strings[5] = "fact,";
        strings[6] = "optimizing";
        strings[7] = "trasdsdsnsitional";
        strings[8] = "Conclusion";
        strings[9] = "Contact";
        int docsInIndex = 1;
        BufferedReader reader = null;
        StringBuffer htmldoc = new StringBuffer();
        StringBuffer origdoc = new StringBuffer();
        try {
            URL url = new URL("http://www.generatescape.com/index.html");
            URLConnection urlConnection = url.openConnection();
            BufferedReader htmlPage1 = new BufferedReader(new InputStreamReader(url.openStream()));
            for (String l = htmlPage1.readLine(); l != null; l = htmlPage1.readLine()) {
                System.out.println(l);
                origdoc.append(l);
            }
            BufferedReader htmlPage2 = new BufferedReader(new InputStreamReader(url.openStream()));
            long tokStartTime = System.currentTimeMillis();
            HTMLParser htmlparser = new HTMLParser(htmlPage2);
            reader = new BufferedReader(htmlparser.getReader());
            LineNumberReader reader2 = new LineNumberReader(htmlparser.getReader());
            for (String l = reader2.readLine(); l != null; l = reader2.readLine()) {
                htmldoc.append(l);
            }
            long tokEndTime = System.currentTimeMillis();
            System.out.println("Tokenization Total time: " + (tokEndTime - tokStartTime) + " ms");
        } catch (Exception e) {
            e.printStackTrace();
        }
        long stopTime1 = System.currentTimeMillis();
        System.out.println("Total time: " + (stopTime1 - 0) + " ms");
        long startTime69 = System.currentTimeMillis();
        StringTokenizer st = new StringTokenizer(htmldoc.toString());
        HashSet set = new HashSet();
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            set.add(token);
        }
        for (int i = 0; i < strings.length; i++) {
            if (set.contains(strings[i])) {
                System.out.println("Found: " + strings[i]);
            }
        }
        long stopTime69 = System.currentTimeMillis();
        System.out.println("Total time: " + (stopTime69 - startTime69) + " ms");
    }
