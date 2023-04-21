    public int calc(Map in, Map results) {
        MathVector zk = (MathVector) in.get("zk");
        MathVector a = (MathVector) in.get("a");
        MathVector lengths = (MathVector) in.get("length");
        int n = (int) ((Fraction) in.get("n")).doubleValue();
        ForMulti fm = new ForMulti();
        int zkSize = zk.size();
        for (int i = 0; i + n < zkSize; i++) {
            double l1 = ((Fraction) lengths.get(i)).doubleValue();
            double e1 = ((Fraction) zk.get(i)).doubleValue();
            for (int j = i + n; j < zkSize; j += n) {
                double l2 = ((Fraction) lengths.get(j)).doubleValue();
                double e2 = ((Fraction) zk.get(j)).doubleValue();
                double subLength = (l1 + l2) / 2;
                double subCentr = Math.abs(e1 - e2);
                if (subCentr >= subLength) {
                    Condition cond = new Condition();
                    cond.setSign(Condition.SIGN_NOTLESS);
                    if (e1 > e2) cond.setLeftPart(Polynomial.parse("x" + (i + 1) + "-x" + (j + 1))); else cond.setLeftPart(Polynomial.parse("x" + (j + 1) + "-x" + (i + 1)));
                    cond.setRightPart(Polynomial.parse(Double.toString(subLength)));
                    fm.addCondition(i + 1, j + 1, cond, n);
                }
            }
        }
        Vector<Condition> obl = new Vector<Condition>();
        for (int i = 0; i < zkSize; i++) {
            Condition cond = new Condition();
            cond.setSign(Condition.SIGN_NOTLESS);
            cond.setLeftPart(Polynomial.parse("x" + (i + 1)));
            cond.setRightPart(Polynomial.parse(Double.toString(((Fraction) lengths.get(i)).doubleValue() / 2)));
            obl.add(cond);
            Condition cond1 = new Condition();
            cond1.setSign(Condition.SIGN_NOTGREATER);
            cond1.setLeftPart(Polynomial.parse("x" + (i + 1)));
            cond1.setRightPart(Polynomial.parse(Double.toString(((Fraction) a.get(i % n)).doubleValue() - ((Fraction) lengths.get(i)).doubleValue() / 2)));
            obl.add(cond1);
        }
        results.put("limitations", fm);
        results.put("obllimitations", obl);
        return 0;
    }
