    @Override
    public void onStep(Actor a) {
        if (a instanceof Expedition && !(a instanceof NonPrincipalExpedition)) {
            switch(UserInterface.getUI().switchChat("Goods Cache", "What do you want to do?", "Fetch Equipment", "Cach� Equipment", "Carry all", "Do Nothing")) {
                case 0:
                    ((ExpeditionUserInterface) UserInterface.getUI()).transferFromCache("Select the goods to transfer", null, this);
                    break;
                case 1:
                    ((ExpeditionUserInterface) UserInterface.getUI()).transferFromExpedition(this);
                    break;
                case 2:
                    Expedition expedition = (Expedition) a;
                    expedition.addAllItemsForced(getInventory());
                    removeAllItems();
                    if (destroyOnEmpty()) getLevel().destroyFeature(this);
                    forceNotSolid = true;
                    break;
                case 3:
            }
        }
    }
