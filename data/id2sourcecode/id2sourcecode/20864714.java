    Collection<GeneSet> run(String annotationURL) throws MalformedURLException, IOException {
        URL url = new URL(annotationURL);
        URLConnection urlc = url.openConnection();
        BufferedReader br = new BufferedReader(new InputStreamReader(new java.util.zip.GZIPInputStream(urlc.getInputStream())));
        String line;
        while ((line = br.readLine()) != null) {
            if (!line.startsWith("!")) {
                String[] records = line.split("\t");
                Integer startId = Integer.valueOf(records[4].substring(3));
                String geneSymbol = records[2];
                ArrayDeque<Integer> queue = new ArrayDeque<Integer>();
                queue.push(startId);
                while (!queue.isEmpty()) {
                    Integer id = queue.pop();
                    Term term = map.get(id);
                    term.geneSet.add(geneSymbol);
                    for (Integer i : term.correspondents) {
                        queue.push(i);
                    }
                }
            }
        }
        ArrayList<GeneSet> list = new ArrayList<GeneSet>(map.size());
        for (Term term : map.values()) {
            list.add(term.geneSet);
        }
        return list;
    }
