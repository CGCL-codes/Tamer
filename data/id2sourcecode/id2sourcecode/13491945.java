    public void deleteSympthom(int sympthomIndex) {
        for (int i = sympthomIndex; i < names.length - 1; i++) {
            names[i] = names[i + 1];
        }
        names[names.length - 1] = "";
        values.remove(sympthomIndex);
        values.add(new Vector<String>());
        fireModifiedListeners();
    }
