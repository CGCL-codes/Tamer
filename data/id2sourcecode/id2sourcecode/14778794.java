    public void addGameObject(GameObject gameObject, String gameObjectSetname) {
        if (gameObject == null || gameObjectSetname == null) {
            throw new NullPointerException("GameLayer.addGameObject: " + "NULL pointer specified.");
        }
        if (gameObjects.containsKey(gameObject.getName())) {
            if (gameObject.gameObjectName.length() > 10 && gameObject.getName().substring(0, 10).compareTo("GameObject") == 0) {
                gameObject.setName("GameObject" + GameObject.UNIQUE_GAMEOBJECT_NEXT_ID++);
            } else {
                throw new IllegalArgumentException("GameLayer.addGameObject: " + "Adding game object with same name as " + "stored game object: " + gameObject.getName());
            }
        }
        if (gameObjectSetname.length() > 0) {
            if (gameObjectCollections.containsKey(gameObjectSetname) == false) {
                throw new IllegalArgumentException("GameLayer.addGameObject: " + "Adding game object with invalid set name: " + gameObjectSetname);
            }
            GameObjectCollection gameObjectCollection = gameObjectCollections.get(gameObjectSetname);
            gameObjectCollection.add(gameObject);
            gameObject.gameObjectCollection = gameObjectCollection;
        }
        gameObjects.put(gameObject.getName(), gameObject);
        int bottomIdx = -1;
        int middleIdx;
        int topIdx = gameObjectsDrawOrderSorted.size();
        int targetDrawOrder = gameObject.getDrawOrder();
        while (topIdx - bottomIdx > 1) {
            middleIdx = (topIdx + bottomIdx) / 2;
            if (gameObjectsDrawOrderSorted.gameObjects[middleIdx].getDrawOrder() < targetDrawOrder) {
                bottomIdx = middleIdx;
            } else {
                topIdx = middleIdx;
            }
        }
        gameObjectsDrawOrderSorted.add(topIdx, gameObject);
    }
