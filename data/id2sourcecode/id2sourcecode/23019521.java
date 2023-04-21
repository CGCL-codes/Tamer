    public int compare(Object a, Object b) {
        if ((a == null) || (b == null)) {
            logResult(0);
            return 0;
        }
        if ((!(a instanceof Program)) || (!(b instanceof Program))) {
            logResult(0);
            return 0;
        }
        Program p1 = (Program) a;
        Program p2 = (Program) b;
        int comp = p1.getChannelId().compareTo(p2.getChannelId());
        if (comp != 0) {
            logResult(comp);
            return comp;
        }
        if (p1.getStartDate() == null) {
            logResult(-1);
            return -1;
        }
        if (p2.getStartDate() == null) {
            logResult(1);
            return 1;
        }
        comp = p1.getStartDate().compareTo(p2.getStartDate());
        logResult(comp);
        return comp;
    }
