    public CavityController(Controller cont, SCLCavity cav) {
        controller = cont;
        sclCavity = cav;
        String llrfName = sclCavity.getId().replaceAll("RF:Cav", "LLRF:FCM");
        llrfName += ":";
        String ampName = llrfName + "cavAmpAvg";
        String autoRunName = llrfName + "RunState";
        ampRBChannel = ChannelFactory.defaultFactory().getChannel(ampName);
        try {
            autoRunHandler = new AutoRunHandler(autoRunName, sclCavity, controller, this);
            phaseRBHandler = new PhaseRBHandler(llrfName + "cavPhaseAvg", sclCavity, controller);
            phaseSetHandler = new PhaseSetHandler(llrfName + "CtlPhaseSet", sclCavity, controller, phaseRBHandler);
        } catch (Exception ex) {
            controller.dumpErr("channel connection error for cavity " + cav.getId());
        }
    }
