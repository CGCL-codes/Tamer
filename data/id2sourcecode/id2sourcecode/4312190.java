    public static List<Performative> createPerformatives(List<List<List>> relationExtsList, List dataRels, WorkItemRecord wir) {
        myLog.debug("CREATEPERFORMATIVES");
        List<Performative> perfs = new ArrayList<Performative>();
        for (List<List> rl : relationExtsList) {
            List relationExt = rl.get(0);
            String sender = (String) relationExt.get(5);
            List<String> receivers = new ArrayList<String>();
            for (List relationExt1 : rl) {
                String receiver = (String) relationExt1.get(3);
                if (!receivers.contains(receiver)) {
                    receivers.add(receiver);
                }
            }
            ProcletPort port = (ProcletPort) relationExt.get(4);
            PortConnection pconn = PortConnections.getInstance().getPortConnectionIPort(port.getPortID());
            List<EntityID> eids = new ArrayList();
            for (List relation : rl) {
                EntityID eid = (EntityID) relation.get(1);
                eids.add(eid);
            }
            String content = calculateContent(eids, wir);
            Performative perf = new Performative(pconn.getChannel(), sender, receivers, "", content, "", ProcletPort.Direction.OUT, eids);
            myLog.debug("perf:" + perf);
            myLog.debug("perfs:" + perfs);
            if (receivers.size() > 1) {
                for (int i = 0; i < receivers.size() - 1; i++) {
                    Performative perfNew = new Performative(perf.getChannel(), perf.getSender(), perf.getReceivers(), perf.getAction(), perf.getContent(), perf.getScope(), perf.getDirection(), perf.getEntityIDs());
                    perfs.add(perfNew);
                }
            }
            perfs.add(perf);
        }
        myLog.debug("perfs:" + perfs);
        return perfs;
    }
