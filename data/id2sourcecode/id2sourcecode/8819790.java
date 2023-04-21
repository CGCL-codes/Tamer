    private boolean findRouteInner(int v1, int f1, int v2, int f2, boolean horizontal, int corners, NamedObject owner, List<Run> runs, long timeout) {
        if (System.currentTimeMillis() > timeout) {
            return false;
        }
        if (corners == 0) {
            return findStraightRoute(v1, v2, f1, f2, horizontal, owner, runs);
        }
        Direction positiveDir, negativeDir;
        if (horizontal) {
            positiveDir = Direction.RIGHT;
            negativeDir = Direction.LEFT;
        } else {
            positiveDir = Direction.UP;
            negativeDir = Direction.DOWN;
        }
        List<Run> bestRuns = null;
        int bestScore = 0;
        int nSteps;
        int v;
        if (corners == 1) {
            nSteps = 0;
            v = v2;
        } else {
            nSteps = Math.abs(v2 - v1) * STEPS_FACTOR + EXTRA_STEPS;
            v = (v1 + v2) / 2;
        }
        for (int i = 0; i <= nSteps; i++, v += (((i & 1) == 1) ? 1 : -1) * i) {
            Direction dir = v > v1 ? positiveDir : negativeDir;
            Run run = new Run(f1, v1, v, dir, owner);
            if (isRunAvailable(run)) {
                List<Run> tail = new ArrayList<Run>(corners);
                tail.add(run);
                addRun(run);
                boolean found = findRouteInner(f1, v, f2, v2, !horizontal, corners - 1, owner, tail, timeout);
                removeRun(run);
                if (found) {
                    int thisScore = getScore(tail);
                    if (bestRuns == null || thisScore < bestScore) {
                        bestRuns = tail;
                        bestScore = thisScore;
                    }
                }
            }
        }
        if (bestRuns != null) {
            runs.addAll(bestRuns);
            return true;
        }
        return false;
    }
