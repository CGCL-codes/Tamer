    public final void addSubserviceCallMetricsToParent(final AlgorithmInput input) {
        for (Class currClass : input.getCustomerClasses()) {
            for (int i = input.getMaxSubserviceLevel(); i >= 0; i--) {
                for (int j = input.getSubserviceParentRelationships().size() - 1; j >= 0; j--) {
                    List<Object> entries = input.getSubserviceParentRelationships().get(j);
                    if (entries.get(0).equals(currClass.getClassID()) && ((Integer) entries.get(3)).intValue() == i) {
                        this.tableMVAResponseTimePerStationAndClass.write((String) entries.get(1), currClass.getClassID(), this.tableMVAResponseTimePerStationAndClass.read((String) entries.get(1), currClass.getClassID()) + this.tableMVAResponseTimePerStationAndClass.read((String) entries.get(2), currClass.getClassID()) / input.getVisitsMap().read((String) entries.get(2), currClass.getClassID()));
                    }
                }
            }
        }
    }
