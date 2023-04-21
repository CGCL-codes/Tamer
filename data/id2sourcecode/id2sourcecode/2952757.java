    private void globalApproach(TablatureSolver solver) {
        TablatureModel model = solver.getTablatureModel();
        AbstractStringInstrument instrument = model.instrument;
        Score score = model.score;
        movesDistances = new MovesDistancesVars(instrument, score);
        Queue<Integer> begins = new LinkedList<Integer>();
        Queue<Integer> ends = new LinkedList<Integer>();
        begins.add(0);
        ends.add(score.getBeatCount() - 1);
        int index = 0;
        while (begins.size() > 0) {
            int begin = begins.remove();
            int end = ends.remove();
            int middle = (begin + end) / 2;
            model.addConstraint(Choco.eq(movesDistances.globalDistances[index], Choco.abs(Choco.minus(model.fretVars[middle], model.fretVars[begin]))));
            index++;
            if (middle - begin > 1) {
                begins.add(begin);
                ends.add(middle);
            }
            if (end - middle > 1) {
                begins.add(middle);
                ends.add(end);
            }
        }
        model.addConstraint(Choco.eq(movesDistances.globalDistances[index], Choco.abs(Choco.minus(model.fretVars[score.getBeatCount() - 1], model.fretVars[score.getBeatCount() / 2]))));
        model.addConstraint(Choco.eq(movesDistances.totalDistance, Choco.sum(movesDistances.globalDistances)));
    }
