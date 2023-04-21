    public double getSimilarity(String term1, String term2) {
        float similarity = 0;
        term1 = term1.toLowerCase();
        term2 = term2.toLowerCase();
        String term1_unique = "";
        String term2_unique = "";
        for (String cterm : term1.split(" ")) {
            boolean is_unique = true;
            for (String cterm2 : term2.split(" ")) if (cterm2.equals(cterm)) {
                is_unique = false;
                break;
            }
            if (is_unique && !Util.isStopWord(cterm)) term1_unique += cterm + " ";
        }
        for (String cterm : term2.split(" ")) {
            boolean is_unique = true;
            for (String cterm2 : term1.split(" ")) if (cterm2.equals(cterm)) {
                is_unique = false;
                break;
            }
            if (is_unique && !Util.isStopWord(cterm)) term2_unique += cterm + " ";
        }
        if (term2_unique.equals("") || term1_unique.equals("")) return 0;
        try {
            float[] vec1 = VectorUtils.getNormalizedVector(CompoundVectorBuilder.getQueryVectorFromString(vecReader, luceneUtils, term1_unique));
            float[] vec2 = VectorUtils.getNormalizedVector(CompoundVectorBuilder.getQueryVectorFromString(vecReader, luceneUtils, term2_unique));
            similarity = VectorUtils.scalarProduct(vec1, vec2);
            similarity = (similarity + 1) / 2;
            if (similarity == 0.5) similarity = 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return similarity;
    }
