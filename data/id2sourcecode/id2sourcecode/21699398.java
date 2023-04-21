    public ActionResult execute(DataHandler dh) {
        ObjectID oid = dh.getCurrentSelectionID();
        GameObject go = dh.getCurrentSelection();
        if (go.objectType() != GameObjectType.UNIT) {
            MainScreen.writeToConsole("Current selection is not an unit");
            return ActionResult.IMPOSSIBLE;
        }
        Unit u = (Unit) go;
        if (u.getMissionFlag()) {
            MainScreen.writeToConsole("Current selection is on a mission");
            return ActionResult.IMPOSSIBLE;
        }
        if (u.getType() != UnitType.COLONIST) {
            MainScreen.writeToConsole("Current selection is not an colonist");
            return ActionResult.IMPOSSIBLE;
        }
        Position p = dh.positionQuery(oid);
        Tile t = dh.positionQuery(p);
        Terrain currentTerrain = t.getTerrain();
        if (currentTerrain.getType() != TerrainType.PLANET) {
            MainScreen.writeToConsole("Colonist must be on a planet to build a base");
            return ActionResult.IMPOSSIBLE;
        }
        if (t.hasType(GameObjectType.BASE)) {
            MainScreen.writeToConsole("Planet already has base on it");
            return ActionResult.IMPOSSIBLE;
        }
        Base b = new Base(currentTerrain.objectID());
        dh.addGameObject(b, p, playerID);
        MainScreen.writeToConsole("Base has been built");
        Player pl = dh.getPlayer(playerID);
        pl.incrementEnergy(((Planet) currentTerrain).getEnergy());
        pl.incrementEnergyUsed(b.getUpkeep());
        u.invalidate();
        dh.returnObject(b);
        dh.returnObject(u);
        return ActionResult.SUCCESS;
    }
