    private void doMoveDown() {
        int which = curveList.getSelectedIndex();
        ObjectInfo swap1 = curve[which];
        curve[which] = curve[which + 1];
        curve[which + 1] = swap1;
        boolean swap2 = reverse[which];
        reverse[which] = reverse[which + 1];
        reverse[which + 1] = swap2;
        Object swap3 = curveList.getItem(which);
        curveList.remove(which);
        curveList.add(which + 1, swap3);
        curveList.setSelected(which + 1, true);
        makeObject();
        updateComponents();
    }
