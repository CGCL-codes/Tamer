    public int attack(int soldiers, int hit, int attack, int woodenTowers, int stoneTowers) {
        this.hit = hit;
        this.attack = attack;
        int leftWoodenSoldiers = 0;
        int leftWoodenRounds = calcRounds(leftWoodenSoldiers, woodenTowers);
        int leftStoneRounds = calcRounds(soldiers - leftWoodenSoldiers, stoneTowers);
        if (leftStoneRounds == Integer.MAX_VALUE) return -1;
        if (leftWoodenRounds == 0) return leftStoneRounds;
        int rightWoodenSoldiers = soldiers;
        int rightWoodenRounds = calcRounds(rightWoodenSoldiers, woodenTowers);
        int rightStoneRounds = calcRounds(soldiers - rightWoodenSoldiers, stoneTowers);
        if (rightWoodenRounds == Integer.MAX_VALUE) return -1;
        if (rightStoneRounds == 0) return rightWoodenRounds;
        while (leftWoodenSoldiers + 1 < rightWoodenSoldiers) {
            int middleWoodenSoldiers = (leftWoodenSoldiers + rightWoodenSoldiers) / 2;
            int middleWoodenRounds = calcRounds(middleWoodenSoldiers, woodenTowers);
            int middleStoneRounds = calcRounds(soldiers - middleWoodenSoldiers, stoneTowers);
            if (middleWoodenRounds - middleStoneRounds > 0) {
                leftWoodenSoldiers = middleWoodenSoldiers;
            } else if (middleWoodenRounds - middleStoneRounds < 0) {
                rightWoodenSoldiers = middleWoodenSoldiers;
            } else return middleWoodenRounds == Integer.MAX_VALUE ? -1 : middleWoodenRounds;
        }
        int rounds = Math.min(Math.max(calcRounds(leftWoodenSoldiers, woodenTowers), calcRounds(soldiers - leftWoodenSoldiers, stoneTowers)), Math.max(calcRounds(rightWoodenSoldiers, woodenTowers), calcRounds(soldiers - rightWoodenSoldiers, stoneTowers)));
        return rounds == Integer.MAX_VALUE ? -1 : rounds;
    }
