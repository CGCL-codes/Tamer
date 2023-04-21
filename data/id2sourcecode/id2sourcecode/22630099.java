    public void transferFromCache(String prompt, GoodType preselectedGoodType, GoodsCache fromCache) {
        ItemTransferFunctionality dualTransferFunctionality = new DualTransferFunctionality(-1, -1);
        transferItems(prompt, preselectedGoodType, fromCache, getExpedition(), dualTransferFunctionality);
        if (fromCache.destroyOnEmpty() && fromCache.getItems().size() == 0) level.destroyFeature(fromCache);
    }
