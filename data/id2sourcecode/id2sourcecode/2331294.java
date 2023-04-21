    public void deleteBranch(int index) {
        branch[index] = null;
        for (int i = index; i < 2; i++) {
            branch[i] = branch[i + 1];
        }
    }
