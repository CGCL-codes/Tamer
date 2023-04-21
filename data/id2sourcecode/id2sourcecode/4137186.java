    public void updateToGame() {
        if (movesTable.isVisible()) {
            long startTime = System.currentTimeMillis();
            Game game = controller.getGame();
            int moveListSize = game.getMoveList().getSize();
            if (moveListSize == 0) {
                movesTable.clearTable();
            } else {
                int numRows = (moveListSize + 1) / 2;
                String[][] data = new String[numRows][2];
                for (int i = 0; i < data.length; i++) {
                    data[i][0] = i + 1 + ") " + GameUtils.convertSanToUseUnicode(game.getMoveList().get(i * 2).toString(), true);
                    if (i * 2 + 1 >= moveListSize) {
                        data[i][1] = "";
                    } else {
                        data[i][1] = GameUtils.convertSanToUseUnicode(game.getMoveList().get(i * 2 + 1).toString(), true);
                    }
                }
                movesTable.refreshTable(data);
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Updated to game in : " + (System.currentTimeMillis() - startTime));
                }
            }
        }
    }
