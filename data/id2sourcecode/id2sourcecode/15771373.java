    public void transferFromCache(String prompt, GoodType preselectedGoodType, GoodsCache cache) {
        List<Equipment> cacheEquipment = cache.getItems();
        Equipment.eqMode = true;
        MenuBox cacheBox = new MenuBox(csi);
        cacheBox.setHeight(13);
        cacheBox.setWidth(54);
        cacheBox.setPosition(24, 7);
        Vector<MenuItem> menuItems = new Vector<MenuItem>();
        for (Equipment item : cacheEquipment) {
            menuItems.add(new CacheItem(item, getExpedition()));
        }
        cacheBox.setMenuItems(menuItems);
        cacheBox.setPromptSize(2);
        cacheBox.setBorder(true);
        cacheBox.setPrompt(prompt + " [Space to exit]");
        cacheBox.setForeColor(ConsoleSystemInterface.RED);
        cacheBox.setBorderColor(ConsoleSystemInterface.TEAL);
        cacheBox.draw();
        while (true) {
            csi.refresh();
            CacheItem itemChoice = ((CacheItem) cacheBox.getSelection());
            if (itemChoice == null) {
                if (cache instanceof ShipCache) {
                    if (getExpedition().getTotalUnits() > 0) break; else {
                        cacheBox.setPrompt("You must first disembark");
                        continue;
                    }
                } else {
                    break;
                }
            }
            Equipment choice = itemChoice.getEquipment();
            ExpeditionItem item = (ExpeditionItem) choice.getItem();
            cacheBox.setPrompt("How many " + item.getDescription() + " will you transfer?");
            cacheBox.draw();
            csi.refresh();
            int quantity = readQuantity(25, 9, "                       ", 5);
            if (quantity == 0) continue;
            if (!(choice.getItem() instanceof ExpeditionUnit) && getExpedition().getTotalUnits() == 0) {
                cacheBox.setPrompt("Someone must receive the goods!");
                cacheBox.draw();
                continue;
            }
            if (quantity > choice.getQuantity()) {
                cacheBox.setPrompt("Not enough " + choice.getItem().getDescription());
                cacheBox.draw();
                continue;
            }
            if (item.getGoodType() != GoodType.PEOPLE && !getExpedition().canCarry(item, quantity)) {
                cacheBox.setPrompt("Your expedition is full!");
                cacheBox.draw();
                continue;
            }
            choice.reduceQuantity(quantity);
            getExpedition().addItem(choice.getItem(), quantity);
            if (choice.getQuantity() == 0) {
                cacheEquipment.remove(choice);
            }
            cacheBox.setPrompt(choice.getItem().getDescription() + " transfered.");
            refresh();
        }
        if (cache.destroyOnEmpty() && cache.getItems().size() == 0) level.destroyFeature(cache);
        Equipment.eqMode = false;
    }
