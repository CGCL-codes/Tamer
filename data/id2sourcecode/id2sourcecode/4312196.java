    public void persistPerformatives() {
        this.deletePerfsFromDB();
        for (Performative perf : perfs) {
            String receiversStr = "";
            for (String receiverStr : perf.getReceivers()) {
                receiversStr = receiversStr + receiverStr + ",";
            }
            receiversStr = receiversStr.substring(0, receiversStr.length() - 1);
            DBConnection.insert(new StoredPerformative(perf.getTime(), perf.getChannel(), perf.getSender(), receiversStr, perf.getAction(), perf.getContent(), perf.getScope(), perf.getDirection().name(), Performatives.parseEntityIDs(perf.getEntityIDs())));
        }
    }
