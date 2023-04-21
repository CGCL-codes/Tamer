    public void removeAtom(int position) {
        for (int i = position; i < atomCount - 1; i++) {
            atoms[i] = atoms[i + 1];
        }
        atoms[atomCount - 1] = null;
        atomCount--;
    }
