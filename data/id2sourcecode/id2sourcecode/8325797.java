    public int refreshStateAtTurnEnd() {
        int deadGeneralId = 0;
        for (int i = 0; i < len; i++) {
            if (soldiers[i].getNum() <= 0) {
                logger.info("ɾ������" + soldiers[i]);
                if (soldiers[i].getType() == SoldierType.WU) {
                    deadGeneralId = soldiers[i].getGeneralId();
                }
                if (soldiers[i].getId() == firstSoldierId) {
                    if (i < len - 1) {
                        firstSoldierId = soldiers[i + 1].getId();
                    } else {
                        firstSoldierId = soldiers[0].getId();
                    }
                }
                for (int j = i; j < len - 1; j++) {
                    soldiers[j] = soldiers[j + 1];
                }
                len--;
            }
        }
        if (soldiers[0].getMove() <= 0) {
            soldiers[0].setAction(SoldierAction.STOP);
            circle();
        }
        return deadGeneralId;
    }
