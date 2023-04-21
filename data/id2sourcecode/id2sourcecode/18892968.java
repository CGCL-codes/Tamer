        public void removePlayer(final short playerId) {
            try {
                GameTaskQueueManager.getManager().render(new Callable<Object>() {

                    public Object call() throws Exception {
                        if (mapOfPlayersIdAndMatrixIndex.containsKey(playerId)) {
                            int index = mapOfPlayersIdAndMatrixIndex.get(playerId);
                            matrix[index][PLAYERNAME_INDEX] = null;
                            matrix[index][TANKNAME_INDEX] = null;
                            matrix[index][TANKMODEL_INDEX] = null;
                            matrix[index][TANKTEAM_INDEX] = null;
                            if (index < (nextIndex - 1)) {
                                for (int i = index; i < nextIndex - 1; i++) {
                                    matrix[i][PLAYERNAME_INDEX] = matrix[i + 1][PLAYERNAME_INDEX];
                                    matrix[i][TANKNAME_INDEX] = matrix[i + 1][TANKNAME_INDEX];
                                    matrix[i][TANKMODEL_INDEX] = matrix[i + 1][TANKMODEL_INDEX];
                                    matrix[i][TANKTEAM_INDEX] = matrix[i + 1][TANKTEAM_INDEX];
                                    if (cbModelsArray[i + 1] != null) {
                                        cbModelsArray[i] = cbModelsArray[i + 1];
                                        cbTeamsArray[i] = cbTeamsArray[i + 1];
                                        int y = 276 - (i * 19);
                                        cbModelsArray[i].setY(y);
                                        cbTeamsArray[i].setY(y);
                                        cbModelsArray[i + 1] = null;
                                        cbTeamsArray[i + 1] = null;
                                    }
                                    matrix[i + 1][PLAYERNAME_INDEX] = null;
                                    matrix[i + 1][TANKNAME_INDEX] = null;
                                    matrix[i + 1][TANKMODEL_INDEX] = null;
                                    matrix[i + 1][TANKTEAM_INDEX] = null;
                                    short actualPlayerId = mapOfMatrixIndexAndPlayersId.get(i + 1);
                                    mapOfPlayersIdAndMatrixIndex.put(actualPlayerId, i);
                                    mapOfMatrixIndexAndPlayersId.remove(i + 1);
                                }
                            }
                            nextIndex--;
                            players.remove(playerId);
                            mapOfPlayersIdAndMatrixIndex.remove(playerId);
                            mapOfMatrixIndexAndPlayersId.remove(index);
                        }
                        return null;
                    }
                }).get();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
