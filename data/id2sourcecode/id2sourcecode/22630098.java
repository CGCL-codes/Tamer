    public void transferFromExpedition(GoodsCache toCache, int destinationMinUnits) {
        ItemTransferFunctionality transferFromExpeditionFunctionality = new DualTransferFunctionality(destinationMinUnits, 1);
        transferItems("Select the goods to transfer", null, getExpedition(), toCache, transferFromExpeditionFunctionality);
    }
