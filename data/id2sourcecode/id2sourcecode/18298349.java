    public DoubleMatrix2D getCloseInInfo(int distance) throws IOException {
        int numSimilarPIs = 0;
        int s = modelElements.size();
        DoubleMatrix2D D = DoubleFactory2D.sparse.make(s, s, 0);
        if (distance < 2) {
            return D;
        }
        Iterator<ProcessInstance> it = log.instanceIterator();
        while (it.hasNext()) {
            ProcessInstance pi = it.next();
            numSimilarPIs = MethodsForWorkflowLogDataStructures.getNumberSimilarProcessInstances(pi);
            int[] memory = new int[distance];
            int i = 0;
            AuditTrailEntryList ates = pi.getAuditTrailEntryList();
            if (ates.size() == 0) {
                continue;
            }
            if (usePOInfo && pi.getAttributes().containsKey(ProcessInstance.ATT_PI_PO) && pi.getAttributes().get(ProcessInstance.ATT_PI_PO).equals("true")) {
                HashMap<String, Integer> ateIDMap = new HashMap(ates.size());
                HashMap<String, Integer> iD2Ate = new HashMap(ates.size());
                Iterator<AuditTrailEntry> ateIt = ates.iterator();
                int index = 0;
                while (ateIt.hasNext()) {
                    AuditTrailEntry ate = ateIt.next();
                    ateIDMap.put(ate.getAttributes().get(ProcessInstance.ATT_ATE_ID), new Integer(modelElements.findLogEventNumber(ate.getElement(), ate.getType())));
                    iD2Ate.put(ate.getAttributes().get(ProcessInstance.ATT_ATE_ID), new Integer(index));
                    index++;
                }
                ateIt = ates.iterator();
                while (ateIt.hasNext()) {
                    AuditTrailEntry ate = ateIt.next();
                    int ateIndex = modelElements.findLogEventNumber(ate.getElement(), ate.getType());
                    findCloseIn(ates, ate, ateIndex, distance, ateIDMap, iD2Ate, D, numSimilarPIs, new ArrayList(distance));
                }
            } else {
                Iterator<AuditTrailEntry> ateIt = ates.iterator();
                while (ateIt.hasNext()) {
                    AuditTrailEntry ate = ateIt.next();
                    if (i < distance) {
                        memory[i] = modelElements.findLogEventNumber(ate.getElement(), ate.getType());
                        i++;
                        continue;
                    }
                    int index = modelElements.findLogEventNumber(ate.getElement(), ate.getType());
                    if (memory[0] == index) {
                        for (int j = 0; j < distance; j++) {
                            D.set(index, memory[j], D.get(index, memory[j]) + numSimilarPIs);
                        }
                    }
                    for (int j = 0; j < distance - 1; j++) {
                        memory[j] = memory[j + 1];
                    }
                    memory[distance - 1] = index;
                }
            }
        }
        return D;
    }
