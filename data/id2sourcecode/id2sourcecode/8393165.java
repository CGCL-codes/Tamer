    private double getSpeed() {
        int punchPower = fighter.getAttributePower(Attribute.punchPower);
        int kickPower = fighter.getAttributePower(Attribute.kickPower);
        int punchReach = fighter.getAttributePower(Attribute.punchReach);
        int kickReach = fighter.getAttributePower(Attribute.kickReach);
        double weight = (punchPower + kickPower) / 2;
        double height = (punchReach + kickReach) / 2;
        return (0.5 * (height - weight) + 5);
    }
