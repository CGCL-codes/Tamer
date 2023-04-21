    private void incrementalSortGameObjectsOnXPosition() {
        GameObject tempGameObject;
        for (int i = 0; i < size - 1; i++) {
            if (gameObjects[i].x - gameObjects[i].boundingDimension / 2 > gameObjects[i + 1].x - gameObjects[i + 1].boundingDimension / 2) {
                tempGameObject = gameObjects[i];
                gameObjects[i] = gameObjects[i + 1];
                gameObjects[i + 1] = tempGameObject;
                int j = i;
                while (j > 0 && gameObjects[j].x - gameObjects[j].boundingDimension / 2 < gameObjects[j - 1].x - gameObjects[j - 1].boundingDimension / 2) {
                    tempGameObject = gameObjects[j];
                    gameObjects[j] = gameObjects[j - 1];
                    gameObjects[j - 1] = tempGameObject;
                    j--;
                }
                j = i + 1;
                while (j < size - 1 && gameObjects[j].x - gameObjects[j].boundingDimension / 2 > gameObjects[j + 1].x - gameObjects[j + 1].boundingDimension / 2) {
                    tempGameObject = gameObjects[j];
                    gameObjects[j] = gameObjects[j + 1];
                    gameObjects[j + 1] = tempGameObject;
                    j++;
                }
            }
        }
    }
