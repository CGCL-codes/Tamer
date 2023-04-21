    private Odds computePreflop(int flops, long holes, int activeOpponents, Card cards[], MersenneTwisterFast rand) {
        Odds odds = new Odds();
        for (int i = 0; i < flops; i++) {
            int xComA = rand.nextInt(COM_A + 1);
            swap(cards, xComA, COM_A);
            int xComB = rand.nextInt(COM_B + 1);
            swap(cards, xComB, COM_B);
            int xComC = rand.nextInt(COM_C + 1);
            swap(cards, xComC, COM_C);
            int xComD = rand.nextInt(COM_D + 1);
            swap(cards, xComD, COM_D);
            int xComE = rand.nextInt(COM_E + 1);
            swap(cards, xComE, COM_E);
            odds = odds.plus(computeOppOdds(activeOpponents, cards, holes, rand));
            swap(cards, xComE, COM_E);
            swap(cards, xComD, COM_D);
            swap(cards, xComC, COM_C);
            swap(cards, xComB, COM_B);
            swap(cards, xComA, COM_A);
        }
        return odds;
    }
