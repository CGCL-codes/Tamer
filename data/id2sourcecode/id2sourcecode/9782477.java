    public ArrayList<int[][]> joinLimiters(int[][][] limiterList) {
        ArrayList<int[][]> joinedLimiters = new ArrayList<int[][]>();
        for (int count = 0; count < limiterList.length; count++) {
            int[][] limiters = limiterList[count];
            if (limiters[1][0] > limiters[1][1]) continue;
            boolean add = true;
            for (int[][] checker : joinedLimiters) {
                if (checker[1][0] == limiters[1][1] + 1) {
                    if (checker[0][0] == limiters[0][0] && checker[0][1] == limiters[0][1] && checker[2][0] == limiters[2][0] && checker[2][1] == limiters[2][1]) {
                        add = false;
                        checker[1][0] = limiters[1][0];
                        break;
                    }
                }
                if (checker[1][1] + 1 == limiters[1][0]) {
                    if (checker[0][0] == limiters[0][0] && checker[0][1] == limiters[0][1] && checker[2][0] == limiters[2][0] && checker[2][1] == limiters[2][1]) {
                        add = false;
                        checker[1][1] = limiters[1][1];
                        break;
                    }
                }
            }
            if (add) {
                joinedLimiters.add(limiters);
            }
        }
        return joinedLimiters;
    }
