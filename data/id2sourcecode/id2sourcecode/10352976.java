    protected int getNextTransition(int state, int letter, int transition) {
        if (transition != Constants.NO) {
            transition++;
            if (transition >= transitionsStored) {
                return (Constants.NO);
            }
            if (transitionsFrom[transition] == state && transitionsLabel.elementAt(transition) == letter) {
                return (transition);
            }
            return (Constants.NO);
        }
        if (stateNumberOfTransitions.elementAt(state) == 0) {
            return (Constants.NO);
        }
        int left = stateTransitions[state];
        int right = left + stateNumberOfTransitions.elementAt(state) - 1;
        int mid;
        while (true) {
            if (left == right) {
                if (transitionsLabel.elementAt(left) == letter) {
                    transition = left;
                    break;
                }
                return (Constants.NO);
            }
            if (left + 1 == right) {
                if (transitionsLabel.elementAt(left) == letter) {
                    transition = left;
                    break;
                }
                if (transitionsLabel.elementAt(right) == letter) {
                    transition = right;
                    break;
                }
                return (Constants.NO);
            }
            mid = (left + right) / 2;
            if (transitionsLabel.elementAt(mid) < letter) {
                left = mid;
            } else if (letter < transitionsLabel.elementAt(mid)) {
                right = mid;
            } else {
                transition = mid;
                break;
            }
        }
        for (; transition > stateTransitions[state] && transitionsLabel.elementAt(transition - 1) == letter; transition--) ;
        return (transition);
    }
