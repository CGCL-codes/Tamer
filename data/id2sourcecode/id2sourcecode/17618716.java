    @Override
    public TreeSet<AnnotationPosition> annotateInstances(String sentence) {
        TreeSet<AnnotationPosition> annotations = new TreeSet<AnnotationPosition>();
        if (sentence == null || sentence.trim().length() == 0) return annotations;
        AnnotationPosition ap = null;
        BagOfWords tkns = new BagOfWords(sentence);
        String token = null;
        StringBuffer sb = new StringBuffer();
        String similarity = "~" + minSimilarity + " ";
        for (int ti = 0; ti < tkns.tokens.length; ++ti) {
            token = tkns.tokens[ti];
            sb.append(token).append(similarity);
        }
        if (sb.length() == 0) return annotations;
        Query query;
        Hits hits;
        try {
            query = parser.parse(sb.toString());
            query = query.rewrite(reader);
            hits = searcher.search(query);
            HashMap<String, AnnotationPosition> matchedEntities = new HashMap<String, AnnotationPosition>();
            int end = Math.min(hits.length(), maxCandidatesConsidered);
            if (hits.length() >= 1) {
                for (int i = 0; i < end; ++i) {
                    if (matchedEntities.get(hits.doc(i).get(nameField)) != null) continue; else {
                        ap = match(sentence, hits.doc(i).get(searchField));
                        if (ap != null) {
                            ap.setNormalised(hits.doc(i).get(nameField));
                            if (ap.getConfidenceRate() >= confidenceThreshold) {
                                annotations.add(ap);
                                matchedEntities.put(hits.doc(i).get(nameField), ap);
                            }
                        }
                    }
                }
            }
        } catch (ParseException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return annotations;
    }
