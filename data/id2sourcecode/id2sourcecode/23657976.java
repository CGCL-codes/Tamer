    protected void townAction(int switchChat, Expedition expedition) {
        switch(switchChat) {
            case 0:
                ((ExpeditionUserInterface) UserInterface.getUI()).transferFromExpedition(this);
                break;
            case 1:
                BuildBuildings buildAction = new BuildBuildings();
                buildAction.setTown(this);
                expedition.setNextAction(buildAction);
                break;
            case 2:
                if (getPopulation() + expedition.getTotalUnits() + 1 <= getLodgingCapacity()) {
                    Hibernate hibernate = new Hibernate(7, true);
                    expedition.setPosition(getPosition().x(), getPosition().y(), getPosition().z());
                    expedition.setNextAction(hibernate);
                } else {
                    expedition.getLevel().addMessage(getDescription() + " can't host all of your expedition.");
                }
            case 3:
                expedition.setPosition(getPosition());
                break;
            case 4:
                break;
        }
    }
