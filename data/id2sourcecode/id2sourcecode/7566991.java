    @Override
    public void execute() {
        Expedition expedition = (Expedition) performer;
        OverworldExpeditionCell standingCell = ((OverworldExpeditionCell) performer.getLevel().getMapCell(performer.getPosition()));
        if (standingCell.isLand()) {
            GoodsCache cache = ((ExpeditionMacroLevel) performer.getLevel()).getOrCreateCache(performer.getPosition());
            performer.setPosition(cache.getPosition());
            ((ExpeditionUserInterface) UserInterface.getUI()).transferFromExpedition(cache);
            if (cache.destroyOnEmpty() && cache.getItems().size() == 0) performer.getLevel().destroyFeature(cache);
        } else {
            Town town = expedition.getDockingTown();
            if (town != null) {
                ((ExpeditionUserInterface) UserInterface.getUI()).transferFromExpedition(town);
            } else if (expedition.canDisembark()) {
                int choice = UserInterface.getUI().switchChat("Landfall", "What do you want to do?", "Land using a predefined group", "Arm people and land using a predefined group", "Select the members of the expedition", "Continue sailing");
                if (choice == 1) {
                    expedition.arm();
                }
                ShipCache ship = new ShipCache((ExpeditionGame) expedition.getGame(), expedition.getCurrentVehicles());
                ship.addAllGoods(expedition);
                switch(choice) {
                    case 0:
                    case 1:
                        LandingParty landingParty = ((ExpeditionUserInterface) UserInterface.getUI()).selectLandingParty();
                        List<Equipment> transferredEquipment = selectUnitsForLanding(landingParty, ship);
                        if (transferredEquipment.size() == 0) {
                            UserInterface.getUI().showImportantMessage("Your expedition doesn't have units to make that landing group.");
                            expedition.removeAllGoods();
                            expedition.setMovementMode(MovementMode.FOOT);
                            expedition.setCurrentVehicles(new ArrayList<Vehicle>());
                            ((ExpeditionUserInterface) UserInterface.getUI()).transferFromCache("Select the units and goods to transfer", GoodType.PEOPLE, ship);
                        } else if (((ExpeditionUserInterface) UserInterface.getUI()).promptUnitList(transferredEquipment, "Landing Group", "These units will disembark, is this ok?")) {
                            expedition.removeAllGoods();
                            expedition.setMovementMode(MovementMode.FOOT);
                            expedition.setCurrentVehicles(new ArrayList<Vehicle>());
                            ship.reduceAllItems(transferredEquipment);
                            expedition.addAllItems(transferredEquipment);
                            if (landingParty.isMounted()) {
                                if (expedition.getItemCountBasic("HORSE") > 0) {
                                    expedition.mount();
                                }
                            }
                            ((ExpeditionUserInterface) UserInterface.getUI()).transferFromCache("Transfer supplies to the expedition", GoodType.SUPPLIES, ship);
                        } else {
                            return;
                        }
                        break;
                    case 2:
                        expedition.removeAllGoods();
                        expedition.setMovementMode(MovementMode.FOOT);
                        expedition.setCurrentVehicles(new ArrayList<Vehicle>());
                        ((ExpeditionUserInterface) UserInterface.getUI()).transferFromCache("Select the units and goods to transfer", GoodType.PEOPLE, ship);
                        break;
                    case 3:
                        return;
                }
                ship.setPosition(new Position(expedition.getPosition()));
                expedition.getLevel().addFeature(ship);
                if (expedition.getUnmountedUnits().size() == 0) {
                    expedition.setMovementMode(MovementMode.HORSE);
                }
                if (expedition.getTotalUnits() == 0) {
                    ship.boardShips(expedition);
                    expedition.getLevel().addMessage("Disembarkment aborted");
                } else {
                    try {
                        expedition.landOn(expedition.getLandCellAround());
                    } catch (ActionCancelException ace) {
                        ace.printStackTrace();
                    }
                }
            } else {
                if (UserInterface.getUI().promptChat("No land nearby; do you want to drop equipment into the sea?")) {
                    GoodsCache cache = new SeaPseudoCache((ExpeditionGame) ((Player) performer).getGame());
                    ((ExpeditionUserInterface) UserInterface.getUI()).transferFromExpedition(cache);
                }
            }
        }
    }
