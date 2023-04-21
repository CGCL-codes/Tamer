    public TableFrame(ExpFile expMain, GrpFile grp, Project project) {
        this.expMain = expMain;
        Vector v = new Vector();
        this.project = project;
        Object o[] = grp.getGroup();
        for (int i = 0; i < o.length; i++) {
            v.addElement(o[i]);
        }
        if (v.size() == 0) jTable = new PrintableTable(expMain, PrintableTable.GRAYSCALE); else jTable = new PrintableTable(expMain, v, PrintableTable.GRAYSCALE);
        jScrollPane1 = new JScrollPane(jTable);
        minvalue = actualmin = expMain.getMinExpValue();
        maxvalue = actualmax = expMain.getMaxExpValue();
        center = minvalue + (maxvalue - minvalue) / 2;
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
