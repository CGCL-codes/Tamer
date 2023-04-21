    private void calculateSpeed() {
        int punchPower = getAttribute(CharacteristicType.punchPower);
        int kickPower = getAttribute(CharacteristicType.kickPower);
        int punchReach = getAttribute(CharacteristicType.punchReach);
        int kickReach = getAttribute(CharacteristicType.kickReach);
        double weight = (punchPower + kickPower) / 2;
        double height = (punchReach + kickReach) / 2;
        speed = (int) Math.abs(0.5 * (height - weight));
    }
