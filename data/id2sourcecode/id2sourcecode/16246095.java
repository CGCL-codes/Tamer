    public String betterHand(Card[] myCards, Card[] opCards, Card[] boardCards) {
        int myHand = -1;
        int opHand = -1;
        Card tempCard;
        String result = "";
        myCombined = new Card[myCards.length + boardCards.length];
        opCombined = new Card[opCards.length + boardCards.length];
        for (int i = 0; i < myCards.length; i++) {
            myCombined[i] = myCards[i];
            opCombined[i] = opCards[i];
        }
        for (int i = 0; i < boardCards.length; i++) {
            myCombined[i + myCards.length] = boardCards[i];
            opCombined[i + opCards.length] = boardCards[i];
        }
        for (int i = 0; i < myCombined.length; i++) {
            for (int j = 0; j < myCombined.length - 1 - i; j++) {
                if (cardValueToInt(myCombined[j + 1].getValue()) > cardValueToInt(myCombined[j].getValue())) {
                    tempCard = myCombined[j];
                    myCombined[j] = myCombined[j + 1];
                    myCombined[j + 1] = tempCard;
                }
                if (cardValueToInt(opCombined[j + 1].getValue()) > cardValueToInt(opCombined[j].getValue())) {
                    tempCard = opCombined[j];
                    opCombined[j] = opCombined[j + 1];
                    opCombined[j + 1] = tempCard;
                }
            }
        }
        myHand = getHand(myCombined);
        opHand = getHand(opCombined);
        System.out.println("MyHand= " + myHand + " OpHand= " + opHand);
        if (myHand < opHand) {
            result = "You Win!";
        } else if (myHand > opHand) {
            result = "You Lose!";
        } else if (myHand == opHand) {
            if (myHand == HIGHCARD && (getHighCard(myCombined) > getHighCard(opCombined))) result = "You Win!"; else if (myHand == HIGHCARD && (getHighCard(myCombined) < getHighCard(opCombined))) result = "You Lose!"; else if (myHand == HIGHCARD && (getHighCard(myCombined) == getHighCard(opCombined))) {
                if (myHand == HIGHCARD && (get2ndHighCard(myCombined) > get2ndHighCard(opCombined))) result = "You Win!"; else if (myHand == HIGHCARD && (get2ndHighCard(myCombined) < get2ndHighCard(opCombined))) result = "You Lose!"; else if (myHand == HIGHCARD && (get2ndHighCard(myCombined) == get2ndHighCard(opCombined))) {
                    if (myHand == HIGHCARD && (get3rdHighCard(myCombined) > get3rdHighCard(opCombined))) result = "You Win!"; else if (myHand == HIGHCARD && (get3rdHighCard(myCombined) < get3rdHighCard(opCombined))) result = "You Lose!"; else if (myHand == HIGHCARD && (get3rdHighCard(myCombined) == get3rdHighCard(opCombined))) {
                        if (myHand == HIGHCARD && (get4thHighCard(myCombined) > get4thHighCard(opCombined))) result = "You Win!"; else if (myHand == HIGHCARD && (get4thHighCard(myCombined) < get4thHighCard(opCombined))) result = "You Lose!"; else if (myHand == HIGHCARD && (get4thHighCard(myCombined) == get4thHighCard(opCombined))) {
                            if (myHand == HIGHCARD && (get5thHighCard(myCombined) > get5thHighCard(opCombined))) result = "You Win!"; else if (myHand == HIGHCARD && (get5thHighCard(myCombined) < get5thHighCard(opCombined))) result = "You Lose!"; else if (myHand == HIGHCARD && (get5thHighCard(myCombined) == get5thHighCard(opCombined))) result = "Push!";
                        }
                    }
                }
            } else if (myHand == ONEPAIR && (getTopPair(myCombined) > getTopPair(opCombined))) result = "You Win!"; else if (myHand == ONEPAIR && (getTopPair(myCombined) < getTopPair(opCombined))) result = "You Lose!"; else if (myHand == ONEPAIR && (getTopPair(myCombined) == getTopPair(opCombined))) {
                if (getHighCard(myCombined) > getHighCard(opCombined)) result = "You Win!"; else if (getHighCard(myCombined) < getHighCard(opCombined)) result = "You Lose!"; else if (getHighCard(myCombined) == getHighCard(opCombined)) {
                    if (get2ndHighCard(myCombined) > get2ndHighCard(opCombined)) result = "You Win!"; else if (get2ndHighCard(myCombined) < get2ndHighCard(opCombined)) result = "You Lose!"; else {
                        if (get3rdHighCard(myCombined) > get3rdHighCard(opCombined)) result = "You Win!"; else if (get3rdHighCard(myCombined) < get3rdHighCard(opCombined)) result = "You Lose!"; else if (get3rdHighCard(myCombined) == get3rdHighCard(opCombined)) result = "Push!";
                    }
                }
            } else if (myHand == TWOPAIR && (getTopPair(myCombined) > getTopPair(opCombined))) result = "You Win!"; else if (myHand == TWOPAIR && (getTopPair(myCombined) < getTopPair(opCombined))) result = "You Lose!"; else if (myHand == TWOPAIR && (getBottomPair(myCombined) > getBottomPair(opCombined))) result = "You Lose!"; else if (myHand == TWOPAIR && (getBottomPair(myCombined) < getBottomPair(opCombined))) result = "You Lose!"; else if (myHand == TWOPAIR && (getHighCard(myCombined) > getHighCard(opCombined))) result = "You Win!"; else if (myHand == TWOPAIR && (getHighCard(myCombined) < getHighCard(opCombined))) result = "You Lose!"; else if (myHand == TWOPAIR && (getHighCard(myCombined) == getHighCard(opCombined))) result = "Push!"; else if (myHand == THREEOFAKIND && (getTopThree(myCombined) > getTopThree(opCombined))) result = "You Win!"; else if (myHand == THREEOFAKIND && (getTopThree(myCombined) < getTopThree(opCombined))) result = "You Lose!"; else if (myHand == FULLHOUSE && (getTopThree(myCombined) > getTopThree(opCombined))) result = "You Win!"; else if (myHand == FULLHOUSE && (getTopThree(myCombined) < getTopThree(opCombined))) result = "You Lose!"; else if (myHand == STRAIGHT && (getHighStraight(myCombined) > getHighStraight(opCombined))) result = "You Win!"; else if (myHand == STRAIGHT && (getHighStraight(myCombined) < getHighStraight(opCombined))) result = "You Lose!"; else if (myHand == STRAIGHT && (getHighStraight(myCombined) == getHighStraight(opCombined))) result = "Push!";
        }
        return result;
    }
