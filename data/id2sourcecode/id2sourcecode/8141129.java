    public MiningResult mineWithProgressSet(LogReader log, LogRelations relations, Progress progress) {
        if (!(log instanceof BufferedLogReader)) {
            Message.add("This Log-reader is READ-ONLY, so the partial order will not be stored." + " Please select another Log-Reader under help.");
        }
        int nrcauspost = 0;
        int nrcauspre = 0;
        int nrtreatpi = 0;
        ArrayList a = new ArrayList();
        LogEvents events = relations.getLogEvents();
        DoubleMatrix2D causal = relations.getCausalFollowerMatrix();
        progress.setNote("updating diagonal");
        for (int i = 0; i < relations.getLogEvents().size(); i++) {
            relations.getParallelMatrix().set(i, i, .0);
            relations.getCausalFollowerMatrix().set(i, i, 1.);
        }
        int overwriteExisting = -1;
        Iterator<ProcessInstance> it = log.instanceIterator();
        while (it.hasNext()) {
            ProcessInstance pi = it.next();
            a.add(pi.getName());
            if (pi.getAttributes().containsKey(ProcessInstance.ATT_PI_PO) && pi.getAttributes().get(ProcessInstance.ATT_PI_PO).equals("true") && (overwriteExisting < 1)) {
                if (overwriteExisting == 0) {
                    continue;
                }
                int i = JOptionPane.showConfirmDialog(MainUI.getInstance(), "This log already contains partially ordered process instances. \n" + "Should this information be overwritten?\n" + "Click YES to overwrite the partial order with a new partial order, \n" + "click NO  to skip all process instances that already contain a partial order.", "Warning", JOptionPane.YES_NO_OPTION);
                if (i == JOptionPane.NO_OPTION) {
                    overwriteExisting = 0;
                    continue;
                }
                if (i == JOptionPane.YES_OPTION) {
                    overwriteExisting = 1;
                }
            }
            nrtreatpi++;
            pi.setAttribute(ProcessInstance.ATT_PI_PO, "true");
            progress.setNote(pi.getName());
            progress.inc();
            int maxIndex = pi.getAuditTrailEntryList().size();
            for (int k = 0; k < maxIndex; k++) {
                AuditTrailEntry ate = null;
                try {
                    ate = pi.getAuditTrailEntryList().get(k);
                    ate.setAttribute(ProcessInstance.ATT_ATE_ID, "id" + k);
                    ate.setAttribute(ProcessInstance.ATT_ATE_POST, "");
                    ate.setAttribute(ProcessInstance.ATT_ATE_PRE, "");
                    pi.getAuditTrailEntryList().replace(ate, k);
                } catch (IOException ex3) {
                } catch (IndexOutOfBoundsException ex3) {
                }
            }
            for (int k = 0; k < maxIndex; k++) {
                AuditTrailEntry ate = null;
                try {
                    ate = pi.getAuditTrailEntryList().get(k);
                } catch (IOException ex) {
                } catch (IndexOutOfBoundsException ex) {
                }
                int ateIndex = events.findLogEventNumber(ate.getElement(), ate.getType());
                for (int i = k + 1; i < maxIndex; i++) {
                    AuditTrailEntry nextAte = null;
                    try {
                        nextAte = pi.getAuditTrailEntryList().get(i);
                    } catch (IOException ex2) {
                    } catch (IndexOutOfBoundsException ex2) {
                    }
                    int nextAteIndex = events.findLogEventNumber(nextAte.getElement(), nextAte.getType());
                    if (causal.get(ateIndex, nextAteIndex) > 0) {
                        String post = ate.getAttributes().get(ProcessInstance.ATT_ATE_POST);
                        String pre = nextAte.getAttributes().get(ProcessInstance.ATT_ATE_PRE);
                        post += (post.length() == 0 ? "" : ",") + nextAte.getAttributes().get(ProcessInstance.ATT_ATE_ID);
                        pre += (pre.length() == 0 ? "" : ",") + ate.getAttributes().get(ProcessInstance.ATT_ATE_ID);
                        ate.setAttribute(ProcessInstance.ATT_ATE_POST, post);
                        nextAte.setAttribute(ProcessInstance.ATT_ATE_PRE, pre);
                        try {
                            pi.getAuditTrailEntryList().replace(nextAte, i);
                        } catch (IOException ex4) {
                        } catch (IndexOutOfBoundsException ex4) {
                        }
                        break;
                    }
                }
                for (int i = k - 1; i >= 0; i--) {
                    AuditTrailEntry prevAte = null;
                    try {
                        prevAte = pi.getAuditTrailEntryList().get(i);
                    } catch (IOException ex2) {
                    } catch (IndexOutOfBoundsException ex2) {
                    }
                    int prevAteIndex = events.findLogEventNumber(prevAte.getElement(), prevAte.getType());
                    if (causal.get(prevAteIndex, ateIndex) > 0) {
                        String pre = ate.getAttributes().get(ProcessInstance.ATT_ATE_PRE);
                        String post = prevAte.getAttributes().get(ProcessInstance.ATT_ATE_POST);
                        post += (post.length() == 0 ? "" : ",") + ate.getAttributes().get(ProcessInstance.ATT_ATE_ID);
                        pre += (pre.length() == 0 ? "" : ",") + prevAte.getAttributes().get(ProcessInstance.ATT_ATE_ID);
                        ate.setAttribute(ProcessInstance.ATT_ATE_PRE, pre);
                        prevAte.setAttribute(ProcessInstance.ATT_ATE_POST, post);
                        try {
                            pi.getAuditTrailEntryList().replace(prevAte, i);
                        } catch (IOException ex4) {
                        } catch (IndexOutOfBoundsException ex4) {
                        }
                        break;
                    }
                }
                try {
                    pi.getAuditTrailEntryList().replace(ate, k);
                } catch (IOException ex1) {
                } catch (IndexOutOfBoundsException ex1) {
                }
            }
            for (int k = 0; k < maxIndex; k++) {
                AuditTrailEntry ate = null;
                try {
                    ate = pi.getAuditTrailEntryList().get(k);
                    String post = ate.getAttributes().get(ProcessInstance.ATT_ATE_POST);
                    StringTokenizer st = new StringTokenizer(post, ",", false);
                    post = "";
                    HashSet s = new HashSet();
                    while (st.hasMoreTokens()) {
                        String tok = st.nextToken();
                        if (s.add(tok)) {
                            post += (post.length() == 0 ? "" : ",") + tok;
                            nrcauspost++;
                        }
                        ;
                    }
                    ate.setAttribute(ProcessInstance.ATT_ATE_POST, post);
                    String pre = ate.getAttributes().get(ProcessInstance.ATT_ATE_PRE);
                    st = new StringTokenizer(pre, ",", false);
                    pre = "";
                    s = new HashSet();
                    while (st.hasMoreTokens()) {
                        String tok = st.nextToken();
                        if (s.add(tok)) {
                            pre += (pre.length() == 0 ? "" : ",") + tok;
                            nrcauspre++;
                        }
                        ;
                    }
                    ate.setAttribute(ProcessInstance.ATT_ATE_PRE, pre);
                    pi.getAuditTrailEntryList().replace(ate, k);
                } catch (IOException ex3) {
                } catch (IndexOutOfBoundsException ex3) {
                }
            }
        }
        Message.add("<PartialOrderGenerator>", Message.TEST);
        Message.add("  <numberOfTreatedInstances=" + nrtreatpi + "/>", Message.TEST);
        Message.add("  <numberOfCausalPosts=" + nrcauspost + "/>", Message.TEST);
        Message.add("  <numberOfCausalPres=" + nrcauspre + "/>", Message.TEST);
        Message.add("</PartialOrderGenerator>", Message.TEST);
        return new PartialOrderMiningResult(log, a);
    }
