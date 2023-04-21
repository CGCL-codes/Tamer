    private Odds computeFlop(int flops, long holes, int activeOpponents, Card cards[], MersenneTwisterFast rand) {
        Odds odds = new Odds();
        if (flops >= 1081) {
            int unknownCount = 52 - 2 - 3;
            FastIntCombiner fc = new FastIntCombiner(Card.INDEXES, unknownCount);
            TurnCommunityVisitor turn = new TurnCommunityVisitor(activeOpponents, cards, holes, rand);
            fc.combine(turn);
            odds = turn.odds();
        } else {
            for (int i = 0; i < flops; i++) {
                int xComD = rand.nextInt(COM_D + 1);
                swap(cards, xComD, COM_D);
                int xComE = rand.nextInt(COM_E + 1);
                swap(cards, xComE, COM_E);
                odds = odds.plus(computeOppOdds(activeOpponents, cards, holes, rand));
                swap(cards, xComE, COM_E);
                swap(cards, xComD, COM_D);
            }
        }
        return odds;
    }
