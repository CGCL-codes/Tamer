    public void ReplaceBlanks(StringBuilder sb, int[] width, int blankAmount) throws Exception {
        int index = 0;
        int nextIndex = 0;
        for (int i = 0; i < width.length; i++) {
            nextIndex = index + width[i];
            if (sb.substring(index, nextIndex).trim().equals("")) {
                int mid = (index + nextIndex) / 2;
                sb.replace(mid, mid + 1, ".");
            }
            index = nextIndex + blankAmount;
        }
    }
