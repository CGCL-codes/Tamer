    private double calculateSpeed(Bot bot) {
        int punchPower = BehaviorIntelligence.getCharacteristic(bot, CHARACTERISTIC_PUNCHPOWER);
        int kickPower = BehaviorIntelligence.getCharacteristic(bot, CHARACTERISTIC_KICKPOWER);
        int punchReach = BehaviorIntelligence.getCharacteristic(bot, CHARACTERISTIC_PUNCHREACH);
        int kickReach = BehaviorIntelligence.getCharacteristic(bot, CHARACTERISTIC_KICKREACH);
        double weight = (punchPower + kickPower) / 2;
        double height = (punchReach + kickReach) / 2;
        return (0.5 * (height - weight)) + 5;
    }
