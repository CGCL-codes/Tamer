    public static int pickPosition(double[] cumProb) {
        int roof = cumProb.length - 1;
        int floor = 1;
        double totalProb = cumProb[roof];
        double r = Math.random() * totalProb;
        int pos = (floor + roof) / 2;
        boolean inUpper = false, inLower = false;
        while (!(inUpper && inLower)) {
            System.out.println("fl=" + floor + " ps=" + pos + " rf=" + roof + "   r=" + r + " !pos=" + cumProb[pos]);
            inUpper = r <= cumProb[pos];
            inLower = r >= cumProb[pos - 1];
            if (!inUpper) {
                floor = pos + 1;
            } else if (!inLower) {
                roof = pos - 1;
            }
            pos = (roof + floor) / 2;
        }
        System.out.println("ret" + pos);
        return pos;
    }
