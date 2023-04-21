    public void transferFromExpedition(GoodsCache ship, int minUnits) {
        List<Equipment> expeditionEquipment = getExpedition().getInventory();
        Equipment.eqMode = true;
        MenuBox cacheBox = new MenuBox(csi);
        cacheBox.setHeight(13);
        cacheBox.setWidth(54);
        cacheBox.setPosition(24, 7);
        Vector menuItems = new Vector();
        for (Equipment item : expeditionEquipment) {
            menuItems.add(new CacheItem(item, ship));
        }
        cacheBox.setMenuItems(menuItems);
        cacheBox.setPromptSize(2);
        cacheBox.setBorder(true);
        cacheBox.setPrompt("Transfer from Expedition to " + ship.getDescription() + " [Space to exit]");
        cacheBox.setForeColor(ConsoleSystemInterface.RED);
        cacheBox.setBorderColor(ConsoleSystemInterface.TEAL);
        cacheBox.draw();
        while (true) {
            csi.refresh();
            CacheItem itemChoice = ((CacheItem) cacheBox.getSelection());
            if (itemChoice == null) {
                if (minUnits != -1) {
                    if (ship.getTotalUnits() < minUnits) {
                        cacheBox.setPrompt("At least " + minUnits + " should be transfered");
                        continue;
                    }
                }
                break;
            }
            Equipment choice = itemChoice.getEquipment();
            ExpeditionItem item = (ExpeditionItem) choice.getItem();
            cacheBox.setPrompt("How many " + item.getDescription() + " will you transfer?");
            cacheBox.draw();
            csi.refresh();
            int quantity = readQuantity(25, 9, "                       ", 5);
            if (quantity == 0) continue;
            if (quantity > choice.getQuantity()) {
                cacheBox.setPrompt("Not enough " + choice.getItem().getDescription());
                cacheBox.draw();
                continue;
            }
            if (!ship.canCarry(item, quantity)) {
                cacheBox.setPrompt("Not enough room in the " + ship.getDescription());
                cacheBox.draw();
                continue;
            }
            getExpedition().reduceQuantityOf(choice.getItem(), quantity);
            if (choice.getItem() instanceof ExpeditionUnit && getExpedition().getCurrentlyCarrying() > 100) {
                cacheBox.setPrompt("The expedition can't carry the goods!");
                cacheBox.draw();
                getExpedition().addItem(choice.getItem(), quantity);
                if (choice.getQuantity() == 0) {
                    menuItems = new Vector();
                    for (Equipment item2 : getExpedition().getInventory()) {
                        menuItems.add(new CacheItem(item2, ship));
                    }
                    cacheBox.setMenuItems(menuItems);
                }
                continue;
            }
            ship.addItem((ExpeditionItem) choice.getItem(), quantity);
            if (choice.getQuantity() == 0) {
                menuItems.remove(choice);
            }
            cacheBox.setPrompt(choice.getItem().getDescription() + " transfered into the " + ship.getDescription());
            refresh();
        }
        Equipment.eqMode = false;
    }
