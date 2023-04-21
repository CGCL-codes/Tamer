    private static CharTrieBase createTrieInternal(CharSequence[] seqs, int pos, int start, int end) {
        if (start >= end) return null;
        int mid = (start + end) / 2;
        char split = seqs[mid].charAt(pos);
        int goRight = mid;
        while (goRight < end && charAt(seqs[goRight], pos) == split) goRight++;
        int goLeft = mid;
        while (goLeft > start && charAt(seqs[goLeft - 1], pos) == split) goLeft--;
        int goLeft2 = goLeft;
        int id = NO_INDEX;
        if (seqs[goLeft].length() == pos + 1) {
            id = goLeft;
            goLeft2++;
        }
        if (start < goLeft || goRight < end) {
            return new FullTree(id, split, createTrieInternal(seqs, pos, start, goLeft), createTrieInternal(seqs, pos + 1, goLeft2, goRight), createTrieInternal(seqs, pos, goRight, end));
        } else {
            return new ForwardTree(start, split, createTrieInternal(seqs, pos + 1, goLeft2, goRight));
        }
    }
