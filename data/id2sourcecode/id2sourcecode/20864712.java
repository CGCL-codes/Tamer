    GeneSetMaker(String ontologyURL) throws MalformedURLException, IOException {
        URL url = new URL(ontologyURL);
        URLConnection urlc = url.openConnection();
        BufferedReader br = new BufferedReader(new InputStreamReader(urlc.getInputStream()));
        map = new HashMap<Integer, Term>();
        Term term;
        while ((term = getTerm(br)) != null) {
            map.put(term.id, term);
        }
        br.close();
    }
