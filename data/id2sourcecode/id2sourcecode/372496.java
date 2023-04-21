    private void buildVectors(int b) {
        Vector helpEi = new Vector();
        Vector helpEi1 = new Vector();
        int helpE;
        int pj;
        int b2 = b;
        int countEi = 0;
        if (b2 < 0) {
            helpEi.addElement(new Integer(1));
            helpEi1.addElement(new Integer(1));
            b2 = -b2;
        } else {
            helpEi.addElement(new Integer(0));
            helpEi1.addElement(new Integer(0));
        }
        for (int i = 1; i != primeBase.size(); i++) {
            pj = ((Integer) primeBase.elementAt(i)).intValue();
            while (b2 % pj == 0) {
                countEi++;
                b2 /= pj;
            }
            helpEi.addElement(new Integer(countEi % 2));
            helpEi1.addElement(new Integer(countEi));
            countEi = 0;
        }
        if (b2 == 1) {
            bSmooth = true;
            ei.setRow(ei.Rsize() - 1, helpEi1);
            vi.setRow(vi.Rsize() - 1, helpEi);
            ei.setSize(ei.Rsize() + 1, ei.Csize());
            vi.setSize(ei.Rsize(), ei.Csize());
        } else bSmooth = false;
        return;
    }
