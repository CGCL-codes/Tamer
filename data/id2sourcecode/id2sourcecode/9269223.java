    private void calculateFighterSpeed() {
        int height = 0, weight = 0;
        preConfigPersonality();
        weight = (punchPower + kickPower) / 2;
        height = (punchReach + kickReach) / 2;
        fighterSpeed = Math.abs(0.5 * (height - weight));
    }
