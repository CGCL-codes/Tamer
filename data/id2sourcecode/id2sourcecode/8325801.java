    public void circle() {
        Soldier soldier = soldiers[0];
        for (int i = 0; i < len - 1; i++) {
            soldiers[i] = soldiers[i + 1];
        }
        soldiers[len - 1] = soldier;
        logger.info(soldier + "�������β��len=[" + this.len + "]");
    }
