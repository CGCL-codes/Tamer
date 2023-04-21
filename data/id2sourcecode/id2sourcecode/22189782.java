    public boolean beginBuilding(UnitType uType) {
        if (this.building == null) {
            MainScreen.writeToConsole("Base: Unit " + uType.toString() + " began building.", Color.GREEN);
            this.building = uType;
            this.mpLeft = uType.manpower();
            return true;
        } else {
            MainScreen.writeToConsole("Base: Base is already building a " + building, Color.GREEN);
            return false;
        }
    }
