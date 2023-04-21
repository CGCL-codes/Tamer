    public static void runSimulationTest2() {
        TransactionResourceManager trm = TransactionResourceManager.getInstance();
        PDP mainPDP = new PDP("PDP1", null);
        PDP sub1PDP = new PDP("PDP1.1", mainPDP);
        PDP sub2PDP = new PDP("PDP1.2", mainPDP);
        PDP sub11PDP = new PDP("PDP1.1.1", sub1PDP);
        PDP sub12PDP = new PDP("PDP1.1.2", sub1PDP);
        PDP sub21PDP = new PDP("PDP1.2.1", sub2PDP);
        {
            ArrayList<PDP> subPdpList = new ArrayList<PDP>();
            subPdpList.add(sub1PDP);
            subPdpList.add(sub2PDP);
            mainPDP.setSubPDPs(subPdpList);
        }
        {
            ArrayList<PDP> subPdpList = new ArrayList<PDP>();
            subPdpList.add(sub11PDP);
            subPdpList.add(sub12PDP);
            sub1PDP.setSubPDPs(subPdpList);
        }
        {
            ArrayList<PDP> subPdpList = new ArrayList<PDP>();
            subPdpList.add(sub21PDP);
            sub2PDP.setSubPDPs(subPdpList);
        }
        String transactionID = "TID-1";
        Transaction t1 = null;
        {
            List<String> readlist = new ArrayList<String>();
            List<String> LPwritelist = new ArrayList<String>();
            List<String> LDwritelist = new ArrayList<String>();
            List<String> GPwritelist = new ArrayList<String>();
            List<String> GDwritelist = new ArrayList<String>();
            readlist.add("ID-1");
            readlist.add("ID-2");
            readlist.add("ID-3");
            GPwritelist.add("ID-2");
            LDwritelist.add("ID-3");
            t1 = new Transaction(transactionID, readlist, LPwritelist, LDwritelist, GPwritelist, GDwritelist, "PDP1", Transaction.COMBINING_ALGORITHM_DENY_OVERRIDES, truetrue);
        }
        mainPDP.addTransaction(t1);
        Transaction t1_1 = null;
        {
            List<String> readlist = new ArrayList<String>();
            List<String> LPwritelist = new ArrayList<String>();
            List<String> LDwritelist = new ArrayList<String>();
            List<String> GPwritelist = new ArrayList<String>();
            List<String> GDwritelist = new ArrayList<String>();
            readlist.add("ID-11");
            readlist.add("ID-12");
            readlist.add("ID-13");
            GPwritelist.add("ID-12");
            LDwritelist.add("ID-13");
            t1_1 = new Transaction(transactionID, readlist, LPwritelist, LDwritelist, GPwritelist, GDwritelist, "PDP1.1", Transaction.COMBINING_ALGORITHM_DENY_OVERRIDES, truetrue);
        }
        sub1PDP.addTransaction(t1_1);
        Transaction t1_2 = null;
        {
            List<String> readlist = new ArrayList<String>();
            List<String> LPwritelist = new ArrayList<String>();
            List<String> LDwritelist = new ArrayList<String>();
            List<String> GPwritelist = new ArrayList<String>();
            List<String> GDwritelist = new ArrayList<String>();
            readlist.add("ID-21");
            readlist.add("ID-22");
            readlist.add("ID-23");
            GPwritelist.add("ID-22");
            LDwritelist.add("ID-23");
            t1_2 = new Transaction(transactionID, readlist, LPwritelist, LDwritelist, GPwritelist, GDwritelist, "PDP1.2", Transaction.COMBINING_ALGORITHM_DENY_OVERRIDES, truetrue);
        }
        sub2PDP.addTransaction(t1_2);
        Transaction t1_1_1 = null;
        {
            List<String> readlist = new ArrayList<String>();
            List<String> LPwritelist = new ArrayList<String>();
            List<String> LDwritelist = new ArrayList<String>();
            List<String> GPwritelist = new ArrayList<String>();
            List<String> GDwritelist = new ArrayList<String>();
            readlist.add("ID-31");
            readlist.add("ID-32");
            readlist.add("ID-33");
            GPwritelist.add("ID-32");
            LDwritelist.add("ID-33");
            t1_1_1 = new Transaction(transactionID, readlist, LPwritelist, LDwritelist, GPwritelist, GDwritelist, "PDP1.1.1", Transaction.COMBINING_ALGORITHM_DENY_OVERRIDES, truetrue);
        }
        sub11PDP.addTransaction(t1_1_1);
        Transaction t1_1_2 = null;
        {
            List<String> readlist = new ArrayList<String>();
            List<String> LPwritelist = new ArrayList<String>();
            List<String> LDwritelist = new ArrayList<String>();
            List<String> GPwritelist = new ArrayList<String>();
            List<String> GDwritelist = new ArrayList<String>();
            readlist.add("ID-41");
            readlist.add("ID-42");
            readlist.add("ID-43");
            GPwritelist.add("ID-42");
            LDwritelist.add("ID-43");
            t1_1_2 = new Transaction(transactionID, readlist, LPwritelist, LDwritelist, GPwritelist, GDwritelist, "PDP1.1.2", Transaction.COMBINING_ALGORITHM_DENY_OVERRIDES, truetrue);
        }
        sub12PDP.addTransaction(t1_1_2);
        Transaction t1_2_1 = null;
        {
            List<String> readlist = new ArrayList<String>();
            List<String> LPwritelist = new ArrayList<String>();
            List<String> LDwritelist = new ArrayList<String>();
            List<String> GPwritelist = new ArrayList<String>();
            List<String> GDwritelist = new ArrayList<String>();
            readlist.add("ID-2");
            readlist.add("ID-52");
            readlist.add("ID-53");
            GPwritelist.add("ID-52");
            LDwritelist.add("ID-53");
            t1_2_1 = new Transaction(transactionID, readlist, LPwritelist, LDwritelist, GPwritelist, GDwritelist, "PDP1.2.1", Transaction.COMBINING_ALGORITHM_DENY_OVERRIDES, falsefalse);
        }
        sub21PDP.addTransaction(t1_2_1);
        {
            Hashtable<String, PDP> pdpTable = new Hashtable<String, PDP>();
            pdpTable.put(mainPDP.getPDPId(), mainPDP);
            pdpTable.put(sub1PDP.getPDPId(), sub1PDP);
            pdpTable.put(sub2PDP.getPDPId(), sub2PDP);
            pdpTable.put(sub11PDP.getPDPId(), sub11PDP);
            pdpTable.put(sub12PDP.getPDPId(), sub12PDP);
            pdpTable.put(sub21PDP.getPDPId(), sub21PDP);
            List<String> pdpOrder = new ArrayList<String>();
            pdpOrder.add(mainPDP.getPDPId());
            pdpOrder.add(sub1PDP.getPDPId());
            pdpOrder.add(sub2PDP.getPDPId());
            pdpOrder.add(sub11PDP.getPDPId());
            pdpOrder.add(sub12PDP.getPDPId());
            pdpOrder.add(sub21PDP.getPDPId());
            trm.setPdpOrder(pdpOrder);
            trm.setPdps(pdpTable);
        }
        Thread trmThread = new Thread(trm);
        trmThread.start();
        {
            Thread mainPDPThread = new Thread(mainPDP);
            mainPDPThread.start();
            Thread sub1PDPThread = new Thread(sub1PDP);
            sub1PDPThread.start();
            Thread sub2PDPThread = new Thread(sub2PDP);
            sub2PDPThread.start();
            Thread sub11PDPThread = new Thread(sub11PDP);
            sub11PDPThread.start();
            Thread sub12PDPThread = new Thread(sub12PDP);
            sub12PDPThread.start();
            Thread sub21PDPThread = new Thread(sub21PDP);
            sub21PDPThread.start();
        }
        TwoPhaseTransactionEvent e = new TwoPhaseTransactionEvent(new Object(), null, transactionID, TwoPhaseTransactionEvent.P1_QUERY_TO_COMMIT);
        mainPDP.enqueEvent(e);
    }
