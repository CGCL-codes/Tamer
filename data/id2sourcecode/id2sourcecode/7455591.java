    public void removeAtom(int position) {
        atoms[position].removeListener(this);
        for (int i = position; i < atomCount - 1; i++) {
            atoms[i] = atoms[i + 1];
        }
        atoms[atomCount - 1] = null;
        atomCount--;
        notifyChanged();
    }
