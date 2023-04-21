    private static short approxMaxOppVal(int atOpp, int activeOpps, Card cards[], int communityShortcut, MersenneTwisterFast rand) {
        if (atOpp >= activeOpps) {
            return Short.MIN_VALUE;
        }
        int holeDestA = OPP_A - atOpp * 2;
        int holeDestB = holeDestA - 1;
        int xOppA = rand.nextInt(holeDestA + 1);
        int xOppB = rand.nextInt(holeDestB + 1);
        swap(cards, xOppA, holeDestA);
        swap(cards, xOppB, holeDestB);
        Card oppA = cards[holeDestA];
        Card oppB = cards[holeDestB];
        short oppVal = Eval7Faster.fastValueOf(communityShortcut, oppA, oppB);
        short maxOtherOppVal = approxMaxOppVal(atOpp + 1, activeOpps, cards, communityShortcut, rand);
        swap(cards, xOppB, holeDestB);
        swap(cards, xOppA, holeDestA);
        return (short) Math.max(oppVal, maxOtherOppVal);
    }
