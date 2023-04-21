    public static final int getMateDepth(int score) {
        int norm_score = Math.abs(score) / ISearch.MAX_MAT_INTERVAL;
        int depth = ISearch.MAX_DEPTH + 1 - norm_score;
        depth = (depth + 1) / 2;
        return score > 0 ? depth : -depth;
    }
