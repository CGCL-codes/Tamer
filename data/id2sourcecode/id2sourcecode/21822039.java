    public int calc(Map in, Map results) {
        MathVector zk = (MathVector) in.get("zk");
        MathVector a = (MathVector) in.get("a");
        MathVector lengths = (MathVector) in.get("length");
        Vector limits = new Vector();
        Vector altlimits = new Vector();
        Vector<Condition> limitations = new Vector<Condition>();
        int zkSize = zk.size();
        for (int i = 0; i < zkSize; i += 2) {
            double l1X = ((Fraction) lengths.get(i)).doubleValue();
            double e1X = ((Fraction) zk.get(i)).doubleValue();
            double l1Y = ((Fraction) lengths.get(i + 1)).doubleValue();
            double e1Y = ((Fraction) zk.get(i + 1)).doubleValue();
            for (int j = i + 2; j < zkSize; j += 2) {
                double l2X = ((Fraction) lengths.get(j)).doubleValue();
                double e2X = ((Fraction) zk.get(j)).doubleValue();
                double subLengthX = (l1X + l2X) / 2;
                double subCentrX = Math.abs(e1X - e2X);
                double l2Y = ((Fraction) lengths.get(j + 1)).doubleValue();
                double e2Y = ((Fraction) zk.get(j + 1)).doubleValue();
                double subLengthY = (l1Y + l2Y) / 2;
                double subCentrY = Math.abs(e1Y - e2Y);
                if (subCentrX >= subLengthX) {
                    Condition cond = new Condition();
                    cond.setSign(Condition.SIGN_NOTLESS);
                    if (e1X > e2X) cond.setLeftPart(Polynomial.parse("x" + (i + 1) + "-x" + (j + 1))); else cond.setLeftPart(Polynomial.parse("x" + (j + 1) + "-x" + (i + 1)));
                    cond.setRightPart(Polynomial.parse(Double.toString(subLengthX)));
                    limits.add(cond);
                } else {
                    limits.add("");
                }
                if (subCentrY >= subLengthY) {
                    Condition cond = new Condition();
                    cond.setSign(Condition.SIGN_NOTLESS);
                    if (e2Y > e1Y) cond.setLeftPart((Polynomial) Polynomial.parse("x" + (i + 2) + "-x" + (j + 2)).mul(-1)); else cond.setLeftPart((Polynomial) Polynomial.parse("x" + (j + 2) + "-x" + (i + 2)).mul(-1));
                    cond.setRightPart(Polynomial.parse(Double.toString(subLengthY)));
                    altlimits.add(cond);
                } else {
                    altlimits.add("");
                }
            }
        }
        for (int i = 0; i < zkSize / 2 + 2; i++) {
            try {
                if (limits.elementAt(i) instanceof Condition) {
                    limitations.add((Condition) limits.elementAt(i));
                } else if (altlimits.get(i) instanceof Condition) {
                    limitations.add((Condition) altlimits.elementAt(i));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        for (int i = 0; i < zkSize / 2 + 2; i++) {
            try {
                if (limits.get(i) instanceof Condition && !(altlimits.get(i) instanceof Condition)) {
                    altlimits.remove(i);
                    altlimits.add(i, limits.get(i));
                } else {
                    if (altlimits.get(i) instanceof Condition && !(limits.get(i) instanceof Condition)) {
                        limits.remove(i);
                        limits.add(i, altlimits.get(i));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        for (int i = 0; i < zkSize; i++) {
            Condition cond = new Condition();
            cond.setSign(Condition.SIGN_NOTLESS);
            cond.setLeftPart(Polynomial.parse("x" + (i + 1)));
            cond.setRightPart(Polynomial.parse(Double.toString(((Fraction) lengths.get(i)).doubleValue() / 2)));
            limitations.add(cond);
            Condition cond1 = new Condition();
            cond1.setSign(Condition.SIGN_NOTGREATER);
            cond1.setLeftPart(Polynomial.parse("x" + (i + 1)));
            cond1.setRightPart(Polynomial.parse(Double.toString(((Fraction) a.get(i % 2)).doubleValue() - ((Fraction) lengths.get(i)).doubleValue() / 2)));
            limitations.add(cond1);
        }
        results.put("limits", limits);
        results.put("altlimits", altlimits);
        results.put("limitations", limitations);
        results.put("zz0", zk.clone());
        return 0;
    }
