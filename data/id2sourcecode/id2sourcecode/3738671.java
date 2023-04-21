    @Override
    public synchronized void ponderHit() {
        if (DEBUGSearch.DEBUG_MODE) {
            if (currentGoCommand == null) {
                throw new IllegalStateException("currentGoCommand == null");
            }
            if (!currentGoCommand.isPonder()) {
                throw new IllegalStateException("!currentGoCommand.isPonder()");
            }
        }
        UCISearchMediatorImpl_StandardPondering ponderMediator = (UCISearchMediatorImpl_StandardPondering) ((SearchMediatorProxy) currentMediator).getParent();
        Go go = ponderMediator.getGoCommand();
        go.setPonder(false);
        ITimeController timeController = TimeControllerFactory.createTimeController(searchAdaptorCfg.getTimeConfig(), boardForSetup.getColourToMove(), go);
        UCISearchMediatorImpl_NormalSearch switchedMediator = new UCISearchMediatorImpl_NormalSearch(ponderMediator.getChannel(), go, timeController, ponderMediator.getColourToMove(), ponderMediator.getBestMoveSender());
        switchedMediator.setLastInfo(ponderMediator.getLastInfo());
        ((SearchMediatorProxy) currentMediator).setParent(switchedMediator);
    }
