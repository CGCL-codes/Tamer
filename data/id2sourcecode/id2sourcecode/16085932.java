    protected SignalProcessorManual initOptimalRehearse() {
        String TIMER_ID = "REHEARSE_TIMER";
        String REHEARSE_ENDER = "REHEARSE_END_CHECK";
        SignalProcessorManual rehearseProg = new SignalProcessorManual(NAME_REHEARSE_PROGRAM);
        TimerProcessor rehearseTimer = new TimerProcessor(TIMER_ID, 2000, true);
        rehearseTimer.setFluctuation(new AbstractRandomGenerator() {

            @Override
            public double getRand() {
                return Network.rand.nextGaussian() * 500;
            }
        });
        TimerProcessor rehearseEnder = new TimerProcessor(REHEARSE_ENDER, Param.tick * 2);
        addProcessor(rehearseTimer);
        addProcessor(rehearseEnder);
        rehearseProg.addRule(Simulation.NAME_SIGNAL_TRIAL_BEGIN, rehearseTimer.getStartName());
        rehearseProg.addRule(new SignalList(rehearseTimer.getGoSignal(), recallState.getOffSignal(), recallWaitState.getOffSignal(), noisyState.getOffSignal(), rehearseState.getOffSignal(), projPRPO.getGate().getOffSignal(), sigNoPhonemeInput, sigNoItemInput, netDR.netProducer.artiBusy.getOffSignal()), new ActionList(rehearseTimer.getOffName(), actRetrieve.getActName(), projPRPO.getCloseActName(), projPRPI.getOpenActName(), netPS.clearWWave.getActName(), netDR.netProducer.artiBlock.getOnName(), psreadState.getOnName(), rehearseState.getOnName(), layerPhonemeInput.getOffActName()));
        rehearseProg.addRule(new SignalList(rehearseState.getOnSignal(), psreadState.getOffSignal(), rewriteState.getOffSignal()), new ActionList(actRetrieveDone.getActName(), rewriteState.getOnName(), netPS.layerWStarter.getOnActName()));
        rehearseProg.addRule(new SignalList(rehearseState.getOnSignal(), RETRIEVE_DONE), new ActionList(netDR.netProducer.artiBlock.getOffName(), netPS.clearPhonemes.getActName(), netDR.retrieveInh.getOnName(), netDR.retrieveCtrl.getOnName()));
        ActionList rewriteDoneAct = new ActionList();
        rewriteDoneAct.add(5, rehearseState.getOffAct());
        rewriteDoneAct.add(5, layerPhonemeInput.getOnAct());
        rewriteDoneAct.add(5, rewriteState.getOffAct());
        rewriteDoneAct.add(5, rehearseTimer.getStartAct());
        rehearseProg.addRule(new SignalList(rewriteState.getOnSignal(), netDR.netProducer.artiDone.getOnSignal()), rewriteDoneAct);
        return rehearseProg;
    }
