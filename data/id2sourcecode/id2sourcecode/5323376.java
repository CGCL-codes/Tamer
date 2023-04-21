    @Override
    public void initAg(String agClass, ClassParameters bbPars, String asSrc, Settings stts) throws JasonException {
        super.initAg(agClass, bbPars, asSrc, stts);
        gui = "yes".equals(stts.getUserParameter("gui"));
        if (getMyId() == 0) gui = true;
        boolean writeStatus = "yes".equals(stts.getUserParameter("write_status"));
        boolean dumpAgsMind = "yes".equals(stts.getUserParameter("dump_ags_mind"));
        if (writeStatus || dumpAgsMind) writeStatusThread = WriteStatusThread.create(this, writeStatus, dumpAgsMind);
        teamId = stts.getUserParameter("teamid");
        if (teamId == null) logger.info("*** No 'teamid' parameter!!!!"); else if (teamId.startsWith("")) teamId = teamId.substring(1, teamId.length() - 1);
        WriteStatusThread.registerAgent(getAgName(), this);
        massimBackDir = stts.getUserParameter("ac_sim_back_dir");
        if (massimBackDir != null && massimBackDir.startsWith("\"")) massimBackDir = massimBackDir.substring(1, massimBackDir.length() - 1);
        logger = Logger.getLogger(CowboyArch.class.getName() + ".CA-" + getAgName());
        setCheckCommunicationLink(false);
        initialBeliefs();
    }
