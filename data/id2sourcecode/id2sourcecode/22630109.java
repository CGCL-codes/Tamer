    public void transferItems(String prompt, GoodType preselectedGoodType, ItemContainer from, ItemContainer to, ItemTransferFunctionality itemTransferFunctionality) {
        Equipment.eqMode = true;
        enterScreen();
        int startX = 480 - 40;
        int gapX = 40;
        CleanButton peopleButton = new CleanButton(IMG_SMALL_BUTTON_BACK, IMG_SMALL_BUTTON_HOVER_BACK, BTN_PEOPLE, HAND_CURSOR);
        peopleButton.setPopupText("People");
        peopleButton.setLocation(startX, 31);
        CleanButton suppliesButton = new CleanButton(IMG_SMALL_BUTTON_BACK, IMG_SMALL_BUTTON_HOVER_BACK, BTN_SUPPLIES, HAND_CURSOR);
        suppliesButton.setLocation(startX + gapX * 1, 31);
        suppliesButton.setPopupText("Supplies");
        CleanButton tradeGoodsButton = new CleanButton(IMG_SMALL_BUTTON_BACK, IMG_SMALL_BUTTON_HOVER_BACK, BTN_MERCHANDISE, HAND_CURSOR);
        tradeGoodsButton.setLocation(startX + gapX * 2, 31);
        tradeGoodsButton.setPopupText("Trade Goods");
        CleanButton armoryButton = new CleanButton(IMG_SMALL_BUTTON_BACK, IMG_SMALL_BUTTON_HOVER_BACK, BTN_WEAPONS, HAND_CURSOR);
        armoryButton.setLocation(startX + gapX * 3, 31);
        armoryButton.setPopupText("Armory");
        CleanButton livestockButton = new CleanButton(IMG_SMALL_BUTTON_BACK, IMG_SMALL_BUTTON_HOVER_BACK, BTN_LIVESTOCK, HAND_CURSOR);
        livestockButton.setLocation(startX + gapX * 4, 31);
        livestockButton.setPopupText("Livestock");
        CleanButton vehiclesButton = new CleanButton(IMG_SMALL_BUTTON_BACK, IMG_SMALL_BUTTON_HOVER_BACK, BTN_VEHICLES, HAND_CURSOR);
        vehiclesButton.setLocation(startX + gapX * 5, 31);
        vehiclesButton.setPopupText("Vehicles");
        CleanButton closeButton = new CleanButton(IMG_SMALL_BUTTON_BACK, IMG_SMALL_BUTTON_HOVER_BACK, BTN_CLOSE, HAND_CURSOR);
        closeButton.setLocation(startX + gapX * 7, 31);
        si.add(peopleButton);
        si.add(suppliesButton);
        si.add(tradeGoodsButton);
        si.add(armoryButton);
        si.add(livestockButton);
        si.add(vehiclesButton);
        si.add(closeButton);
        BlockingQueue<String> transferFromExpeditionHandler = new LinkedBlockingQueue<String>();
        peopleButton.addActionListener(getStringCallBackActionListener(transferFromExpeditionHandler, "GOOD_TYPE:0"));
        suppliesButton.addActionListener(getStringCallBackActionListener(transferFromExpeditionHandler, "GOOD_TYPE:1"));
        tradeGoodsButton.addActionListener(getStringCallBackActionListener(transferFromExpeditionHandler, "GOOD_TYPE:2"));
        armoryButton.addActionListener(getStringCallBackActionListener(transferFromExpeditionHandler, "GOOD_TYPE:3"));
        livestockButton.addActionListener(getStringCallBackActionListener(transferFromExpeditionHandler, "GOOD_TYPE:4"));
        vehiclesButton.addActionListener(getStringCallBackActionListener(transferFromExpeditionHandler, "GOOD_TYPE:5"));
        closeButton.addActionListener(getStringCallBackActionListener(transferFromExpeditionHandler, "BREAK"));
        CallbackKeyListener<String> cbkl = new CallbackKeyListener<String>(transferFromExpeditionHandler) {

            @Override
            public void keyPressed(KeyEvent e) {
                try {
                    CharKey x = new CharKey(SwingSystemInterface.charCode(e));
                    if (x.code == CharKey.SPACE || x.code == CharKey.ENTER) {
                        handler.put("CONFIRM_TRANSFER");
                    } else if (x.code == CharKey.ESC) {
                        handler.put("BREAK");
                    } else if (x.code == CharKey.N1) {
                        handler.put("GOOD_TYPE:0");
                    } else if (x.code == CharKey.N2) {
                        handler.put("GOOD_TYPE:1");
                    } else if (x.code == CharKey.N3) {
                        handler.put("GOOD_TYPE:2");
                    } else if (x.code == CharKey.N4) {
                        handler.put("GOOD_TYPE:3");
                    } else if (x.code == CharKey.N5) {
                        handler.put("GOOD_TYPE:4");
                    } else if (x.code == CharKey.N6) {
                        handler.put("GOOD_TYPE:5");
                    }
                } catch (InterruptedException e1) {
                }
            }
        };
        si.addKeyListener(cbkl);
        TransferBorderGridBox menuBox = new TransferBorderGridBox(BORDER1, BORDER2, BORDER3, BORDER4, si, COLOR_WINDOW_BACKGROUND, COLOR_BORDER_IN, COLOR_BORDER_OUT, borderSize, 6, 9, 12, STANDARD_ITEM_HEIGHT, STANDARD_ITEM_WIDTH, 2, 6, IMG_BOX, null, from, to, transferFromExpeditionHandler, itemTransferFunctionality);
        menuBox.setCursor(si.getCursor());
        menuBox.setBounds(16, 16, 768, 480);
        menuBox.setTitle(itemTransferFunctionality.getTitle(from, to));
        menuBox.setLegend(prompt);
        int typeChoice = 0;
        int selectedIndex;
        GoodType[] goodTypes = GoodType.getGoodTypes();
        if (preselectedGoodType != null) {
            int index = 0;
            for (GoodType goodType : goodTypes) {
                if (preselectedGoodType.equals(goodType)) typeChoice = index;
                index++;
            }
        }
        Map<GoodType, List<ExpeditionItem>> expeditionItemMap = null;
        expeditionItemMap = new HashMap<GoodType, List<ExpeditionItem>>();
        for (GoodType goodType : goodTypes) {
            expeditionItemMap.put(goodType, new ArrayList<ExpeditionItem>());
            List<Equipment> fromGoods = from.getGoods(goodType);
            for (Equipment fromGood : fromGoods) {
                expeditionItemMap.get(goodType).add((ExpeditionItem) fromGood.getItem());
            }
            List<Equipment> toGoods = to.getGoods(goodType);
            for (Equipment toGood : toGoods) {
                if (expeditionItemMap.get(goodType) == null) {
                    expeditionItemMap.put(goodType, new ArrayList<ExpeditionItem>());
                }
                if (!expeditionItemMap.get(goodType).contains((ExpeditionItem) toGood.getItem())) {
                    expeditionItemMap.get(goodType).add((ExpeditionItem) toGood.getItem());
                }
            }
        }
        while (true) {
            menuBox.setHoverDisabled(false);
            int currentPage = menuBox.getCurrentPage();
            List<ExpeditionItem> inventory = null;
            if (typeChoice < goodTypes.length) {
                inventory = expeditionItemMap.get(goodTypes[typeChoice]);
            }
            Vector<CustomGFXMenuItem> menuItems = new Vector<CustomGFXMenuItem>();
            for (ExpeditionItem item : inventory) {
                switch(itemTransferFunctionality.getMenuItemType()) {
                    case CACHE:
                        menuItems.add(new CacheCustomGFXMenuItem(item, from, to));
                        break;
                    case STORE:
                        menuItems.add(new StoreCustomGFXMenuItem(item, ((BuyItemsFunctionality) itemTransferFunctionality).store, getExpedition()));
                        break;
                }
            }
            Collections.sort(menuItems, ITEMS_COMPARATOR);
            menuBox.setMenuItems(menuItems);
            if (menuBox.isValidPage(currentPage)) {
                menuBox.setCurrentPage(currentPage);
                menuBox.updatePageButtonStatus();
            } else {
                currentPage = menuBox.getPages() - 1;
                menuBox.setCurrentPage(currentPage);
                menuBox.updatePageButtonStatus();
            }
            int boxX = startX + typeChoice * gapX - 21;
            menuBox.draw(true, boxX);
            String command = null;
            while (command == null) {
                try {
                    command = transferFromExpeditionHandler.take();
                } catch (InterruptedException ie) {
                }
            }
            menuBox.setHoverDisabled(true);
            String[] commandParts = command.split(":");
            if (commandParts[0].equals("GOOD_TYPE")) {
                int currentType = typeChoice;
                if (commandParts[1].equals("<")) {
                    typeChoice--;
                    if (typeChoice == -1) typeChoice = 0;
                    if (currentType != typeChoice) {
                        menuBox.resetSelection();
                    }
                    continue;
                } else if (commandParts[1].equals(">")) {
                    typeChoice++;
                    if (typeChoice == goodTypes.length) typeChoice = goodTypes.length - 1;
                    if (currentType != typeChoice) {
                        menuBox.resetSelection();
                    }
                    continue;
                } else {
                    typeChoice = Integer.parseInt(commandParts[1]);
                    if (currentType != typeChoice) {
                        menuBox.resetSelection();
                    }
                    continue;
                }
            } else if (commandParts[0].equals("BREAK")) {
                if (!itemTransferFunctionality.validateBreak(from, to)) {
                    transferFromExpeditionHandler.clear();
                    continue;
                }
                break;
            } else if (commandParts[0].equals("SELECT_UNIT")) {
                selectedIndex = Integer.parseInt(commandParts[1]);
                menuBox.selectUnit(selectedIndex);
            } else if (commandParts[0].equals("CHANGE_PAGE")) {
            }
        }
        si.remove(peopleButton);
        si.remove(suppliesButton);
        si.remove(tradeGoodsButton);
        si.remove(armoryButton);
        si.remove(livestockButton);
        si.remove(vehiclesButton);
        si.remove(closeButton);
        si.removeKeyListener(cbkl);
        menuBox.kill();
        Equipment.eqMode = false;
        leaveScreen();
        si.loadLayer(getUILayer());
    }
