    protected void collectSites(RestrictionEnzyme re, Vector new_positions, Vector all_positions, int strand) {
        int match_cnt = new_positions.size();
        for (int i = 0; i < match_cnt; i++) {
            int[] match_positions = (int[]) new_positions.elementAt(i);
            int cut_count = all_positions.size();
            boolean palindrome = false;
            for (int j = 0; j < cut_count && !palindrome; j++) {
                CutSite cutsite = (CutSite) all_positions.elementAt(j);
                palindrome = (match_positions[0] == cutsite.getLow() && match_positions[1] == cutsite.getHigh());
            }
            if (!palindrome) {
                CutSite match = (strand == 1 ? new CutSite(match_positions[0], match_positions[1], re) : new CutSite(match_positions[1], match_positions[0], re));
                match.setRefSequence(re.getRefSequence());
                all_positions.add(match);
                SeqFeatureUtil.sort(all_positions, 1);
            }
        }
    }
