    private static PDP recursivelyConstructPDPs(int noOfNodes, int currentDepth, int conflictsBetweenNodes, int connectivity, PDP parentPdp) {
        String pdpId = "PDP-" + nodeid;
        int myid = nodeid;
        nodeid++;
        PDP pdp = new PDP(pdpId, parentPdp);
        pdpList.add(pdp);
        pdpTable.put(pdp.getPDPId(), pdp);
        pdpOrder.add(pdp.getPDPId());
        currentDepth--;
        if (currentDepth > 0) {
            for (int i = 0; i < connectivity; i++) {
                List<PDP> subPdpList = null;
                if (pdp.getSubPDPs() != null) {
                    subPdpList = pdp.getSubPDPs();
                } else {
                    subPdpList = new ArrayList<PDP>();
                }
                if ((nodeid) < noOfNodes) {
                    System.out.println(pdpId + ": adding child (" + (subPdpList.size() + 1) + "): " + (nodeid));
                    PDP child = recursivelyConstructPDPs(noOfNodes, currentDepth, conflictsBetweenNodes, connectivity, pdp);
                    subPdpList.add(child);
                    pdp.setSubPDPs(subPdpList);
                } else {
                    if (subPdpList.size() == 0) {
                        System.out.println(pdpId + ": end PDP");
                    } else {
                        System.out.println(pdpId + ": done adding (" + subPdpList.size() + ") children");
                    }
                    break;
                }
            }
        } else {
            System.out.println(pdpId + ": end PDP");
        }
        Transaction t = null;
        {
            List<String> readlist = new ArrayList<String>();
            List<String> LPwritelist = new ArrayList<String>();
            List<String> LDwritelist = new ArrayList<String>();
            List<String> GPwritelist = new ArrayList<String>();
            List<String> GDwritelist = new ArrayList<String>();
            int READSIZE = 1;
            for (int i = 0; i < READSIZE; i++) {
                readlist.add("ID-" + (myid + conflictsBetweenNodes + 1));
            }
            if ((myid + 1) >= (noOfNodes - conflictsBetweenNodes)) {
                for (int i = 0; i < conflictsBetweenNodes; i++) {
                    readlist.add("ID-" + (i + 1));
                    GPwritelist.add("ID-" + (i + 1));
                    LDwritelist.add("ID-" + (i + 1));
                }
            }
            t = new Transaction("TransactionID1", readlist, LPwritelist, LDwritelist, GPwritelist, GDwritelist, pdpId, Transaction.COMBINING_ALGORITHM_DENY_OVERRIDES, truetrue);
        }
        pdp.addTransaction(t);
        return pdp;
    }
