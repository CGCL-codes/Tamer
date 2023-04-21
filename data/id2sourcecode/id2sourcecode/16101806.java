    public void addEvent(LogEvent event, Template t, Predicate pred) {
        ConstraintMatrix cm = cms.getConstraintMatrix(t.getFormulaId());
        if (cm != null) {
            long A[][] = cm.getA();
            long B[] = cm.getB();
            int colIndex = cm.getColumnIndex(pred.getTimeVarName());
            for (int i = 0; i < cm.getRowCount(); i++) {
                if (A[i][colIndex] != 0) {
                    B[i] = B[i] - (A[i][colIndex] * event.getTimestamp());
                    A[i][colIndex] = 0;
                }
            }
            String chN = "";
            double maxE = 0;
            boolean found = false;
            for (int i = 0; i < t.totalPredicates(); i++) {
                Predicate p = t.getPredicate(i);
                if (!p.getTimeVarName().equals(pred.getTimeVarName())) {
                    Simplex LP = new Simplex(cm.getColumnCount(), cm.getRowCount());
                    float coefficients[] = new float[cm.getColumnCount()];
                    float rhs;
                    if (!p.getTimeVarName().equals(pred.getTimeVarName())) {
                        int col = cm.getColumnIndex(p.getTimeVarName());
                        for (int c = 0; c < cm.getColumnCount(); c++) if (c == col) coefficients[c] = 1; else coefficients[c] = 0;
                        LP.specifyObjective(coefficients, false);
                        for (int r = 0; r < cm.getRowCount(); r++) {
                            int sign = 1;
                            if (B[r] < 0) sign = -1;
                            for (int c = 0; c < cm.getColumnCount(); c++) coefficients[c] = A[r][c] * sign;
                            rhs = B[r] * sign;
                            LP.addConstraint(coefficients, rhs, 0);
                        }
                        LP.preprocess(cm.getColumnCount(), cm.getRowCount());
                        int SolveStatus;
                        boolean done = false;
                        double maxT = 0;
                        while (!done) {
                            SolveStatus = LP.iterate();
                            if (SolveStatus == LP.Unbounded) {
                                done = true;
                            } else if (SolveStatus == LP.Optimal) {
                                if (LP.artificialPresent == false) {
                                    done = true;
                                    found = true;
                                    maxT = LP.objectiveValue;
                                } else {
                                    if (LP.calculateObjective() == 0) {
                                        LP.getRidOfArtificials();
                                    } else {
                                        done = true;
                                    }
                                }
                            }
                        }
                        if (found) {
                            if (maxT > maxE) {
                                maxE = maxT;
                                chN = cm.getChannelName(p.getTimeVarName());
                            }
                        }
                    }
                }
            }
            if (!chN.equals("")) {
                eventLog.add(new EventTriplet(event, maxE, chN));
            }
        }
    }
