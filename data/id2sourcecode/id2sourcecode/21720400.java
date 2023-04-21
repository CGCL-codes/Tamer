    public void removeTreeModelListener(TreeModelListener arg0) {
        for (int i = 0; i < tcount; i++) {
            if (tmls[i] == arg0) {
                tmls[i] = tmls[i + 1];
                arg0 = tmls[i];
            }
        }
        tcount--;
    }
