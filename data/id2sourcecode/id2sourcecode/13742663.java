    public List getAllUsages(boolean read, boolean write) {
        List allUsages = getAllUsages();
        if (read && write) {
            return allUsages;
        } else {
            List usages = new ArrayList();
            for (Iterator i = allUsages.iterator(); i.hasNext(); ) {
                EncapsulationInvocationData data = (EncapsulationInvocationData) i.next();
                BinSourceConstruct sourceConstruct = (BinSourceConstruct) data.getInConstruct();
                if ((read && (BinVariableSearchFilter.isReadAccess(sourceConstruct))) || (write && (BinVariableSearchFilter.isWriteAccess(sourceConstruct)))) {
                    usages.add(data);
                }
            }
            return usages;
        }
    }
