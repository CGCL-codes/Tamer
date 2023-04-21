    private List<String> getNGram(String[] tokens, int n, boolean includeSmaller) {
        List<Integer[]> ng = new ArrayList<Integer[]>();
        int begin = 0;
        int end = begin + n - 1 >= tokens.length ? tokens.length - 1 : begin + n - 1;
        Integer positions[] = new Integer[2];
        while (end < tokens.length) {
            positions = new Integer[2];
            positions[0] = begin;
            positions[1] = end;
            ng.add(positions);
            if (includeSmaller) {
                int smallEnd = end - 1;
                while (smallEnd >= begin) {
                    positions = new Integer[2];
                    positions[0] = begin;
                    positions[1] = smallEnd;
                    ng.add(positions);
                    smallEnd--;
                }
            }
            begin++;
            end++;
        }
        if (includeSmaller) {
            while (begin != end) {
                int smallEnd = end - 1;
                while (smallEnd >= begin && smallEnd < tokens.length) {
                    positions = new Integer[2];
                    positions[0] = begin;
                    positions[1] = smallEnd;
                    ng.add(positions);
                    smallEnd--;
                }
                begin++;
            }
        }
        List<String> ngrams = new ArrayList<String>();
        for (Integer[] offsets : ng) {
            String ngram = "";
            for (int i = offsets[0]; i <= offsets[1]; i++) {
                ngram = ngram + tokens[i] + " ";
            }
            ngrams.add(ngram.trim());
        }
        return ngrams;
    }
