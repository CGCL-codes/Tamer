    private edu.cmu.sphinx.decoder.linguist.SentenceHMMState expandPronunciation(edu.cmu.sphinx.decoder.linguist.PronunciationState state) {
        Pronunciation pronunciation = state.getPronunciation();
        Unit[] units = pronunciation.getUnits();
        edu.cmu.sphinx.decoder.linguist.SentenceHMMState lastState = state;
        for (int i = 0; i < units.length; i++) {
            edu.cmu.sphinx.decoder.linguist.UnitState unitState = null;
            if (i == 0 || i == (units.length - 1)) {
                unitState = new edu.cmu.sphinx.decoder.linguist.UnitState(state, i, units[i]);
            } else {
                Unit[] leftContext = new Unit[1];
                Unit[] rightContext = new Unit[1];
                leftContext[0] = units[i - 1];
                rightContext[0] = units[i + 1];
                Context context = LeftRightContext.get(leftContext, rightContext);
                Unit unit = new Unit(units[i].getName(), units[i].isFiller(), context);
                unitState = new edu.cmu.sphinx.decoder.linguist.UnitState(state, i, unit);
            }
            attachState(lastState, unitState, logMath.getLogOne(), logMath.getLogOne(), unitInsertionProbability);
            lastState = expandUnit(unitState);
            if (unitState.getUnit().isSilence()) {
                attachState(lastState, unitState, logMath.getLogOne(), logMath.getLogOne(), logMath.getLogOne());
            }
        }
        Unit lastUnit = units[units.length - 1];
        if (addSelfLoopWordEndSilence && !lastUnit.isSilence()) {
            addLoopSilence(lastState, state);
        }
        return lastState;
    }
