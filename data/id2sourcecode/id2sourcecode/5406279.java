    public static final void showByParameters() {
        PrintStream ps;
        OUT = Parameters.getBooleanParameter("debug.OUT", OUT);
        lineSeparator = Parameters.getOptionalParameter("line.separator", lineSeparator);
        if (Parameters.getBooleanParameter("debug.show.all")) showAll();
        if (Parameters.getBooleanParameter("debug.show.most")) showAllExceptMassInfo();
        showExceptions = Parameters.getBooleanParameter("debug.show.exceptions", showExceptions);
        showErrors = Parameters.getBooleanParameter("debug.show.errors", showErrors);
        showWarnings = Parameters.getBooleanParameter("debug.show.warnings", showWarnings);
        showRuns = Parameters.getBooleanParameter("debug.show.runs", showRuns);
        showInfo = Parameters.getBooleanParameter("debug.show.info", showInfo);
        showSQLInfo = Parameters.getBooleanParameter("debug.show.info.sql", showSQLInfo);
        showBigInfo = Parameters.getBooleanParameter("debug.show.info.big", showBigInfo);
        showEventInfo = Parameters.getBooleanParameter("debug.show.info.event", showEventInfo);
        if (Parameters.getBooleanParameter("debug.show.additional")) showAdditional();
        addDate = Parameters.getBooleanParameter("debug.show.additional.date", addDate);
        addTime = Parameters.getBooleanParameter("debug.show.additional.time", addTime);
        addName = Parameters.getBooleanParameter("debug.show.additional.name", addName);
        addKind = Parameters.getBooleanParameter("debug.show.additional.kind", addKind);
        extraErrorChannel = Parameters.getBooleanParameter("debug.extra.error", extraErrorChannel);
        extraWarningChannel = Parameters.getBooleanParameter("debug.extra.warning", extraWarningChannel);
        extraInfoChannel = Parameters.getBooleanParameter("debug.extra.info", extraInfoChannel);
        for (int i = 0; i < channels; i++) {
            ps = getPrintStream(Parameters.getParameter("debug.redirect." + i));
            if (ps != null) channel[i] = ps;
        }
        ps = getPrintStream(Parameters.getParameter("debug.redirect.all"));
        if (ps != null) for (int i = 0; i < channels; i++) channel[i] = ps;
        errorChannel = getChannel("debug.map.error", errorChannel);
        warningChannel = getChannel("debug.map.warning", warningChannel);
        infoChannel = getChannel("debug.map.info", infoChannel);
        runChannel = getChannel("debug.map.run", runChannel);
        allChannel = getChannel("debug.map.all", allChannel);
    }
