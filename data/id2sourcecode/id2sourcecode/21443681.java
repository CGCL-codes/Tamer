    private static void setReverseOrder(AlignmentI align, SequenceI[] seqs) {
        int nSeq = seqs.length;
        int len;
        if (nSeq % 2 == 0) {
            len = nSeq / 2;
        } else {
            len = (nSeq + 1) / 2;
        }
        for (int i = 0; i < len; i++) {
            align.getSequences().setElementAt(seqs[nSeq - i - 1], i);
            align.getSequences().setElementAt(seqs[i], nSeq - i - 1);
        }
    }
