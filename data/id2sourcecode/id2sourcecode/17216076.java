    public int calculateSpeed(int pPower, int kPower, int pReach, int kReach) {
        int weight = (pPower + kPower) / 2;
        int height = (pReach + kReach) / 2;
        int speed = (int) ((height - weight) * 0.5);
        if (speed <= 0) speed = 1;
        return speed;
    }
