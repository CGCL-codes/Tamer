    private edu.cmu.sphinx.decoder.linguist.SentenceHMMState expandPronunciation(edu.cmu.sphinx.decoder.linguist.PronunciationState state) {
        Pronunciation pronunciation = state.getPronunciation();
        Unit[] units = pronunciation.getUnits();
        edu.cmu.sphinx.decoder.linguist.SentenceHMMState combineState = new CombineState(state.getParent(), state.getWhich());
        combineState.setColor(Color.RED);
        for (int a = 0; a < acousticModels.length; a++) {
            AcousticModel model = acousticModels[a];
            edu.cmu.sphinx.decoder.linguist.SentenceHMMState lastState = state;
            for (int i = 0; i < units.length; i++) {
                ParallelUnitState unitState = null;
                if (i == 0 || i == (units.length - 1)) {
                    unitState = new ParallelUnitState(state, model.getName(), i, units[i], tokenStackCapacity);
                } else {
                    Unit[] leftContext = new Unit[1];
                    Unit[] rightContext = new Unit[1];
                    leftContext[0] = units[i - 1];
                    rightContext[0] = units[i + 1];
                    Context context = LeftRightContext.get(leftContext, rightContext);
                    Unit unit = new Unit(units[i].getName(), units[i].isFiller(), context);
                    unitState = new ParallelUnitState(state, model.getName(), i, unit, tokenStackCapacity);
                }
                unitState.setColor(Color.GREEN);
                attachState(lastState, unitState, logMath.getLogOne(), logMath.getLogOne(), unitInsertionProbability);
                lastState = expandUnit(unitState, model);
                if (unitState.getUnit().isSilence()) {
                    attachState(lastState, unitState, logMath.getLogOne(), logMath.getLogOne(), logMath.getLogOne());
                }
            }
            Unit lastUnit = units[units.length - 1];
            if (addSelfLoopWordEndSilence && !lastUnit.isSilence()) {
                addLoopSilence(lastState, state, model);
            }
            attachState(lastState, combineState, logMath.getLogOne(), logMath.getLogOne(), logMath.getLogOne());
        }
        return combineState;
    }
