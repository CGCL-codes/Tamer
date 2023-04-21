    public int demote(Move move, int num) {
        if (num < 0) throw new IllegalArgumentException("cannot perform negative demotion");
        Move tmp = null;
        int newIndex = 0;
        int oldIndex = getIndex(move);
        if (oldIndex == -1) throw new NullPointerException("Move is not a current variation"); else move = branches[oldIndex];
        if (num != 0) newIndex = oldIndex + num; else newIndex = branches.length - 1;
        if (newIndex >= branches.length) throw new ArrayIndexOutOfBoundsException("Move cannot be demoted beyond the end of the list");
        for (int i = oldIndex; i < newIndex; i++) {
            branches[i] = branches[i + 1];
        }
        branches[newIndex] = move;
        return newIndex;
    }
