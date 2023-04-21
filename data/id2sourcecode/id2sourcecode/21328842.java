    public void execute(IntelligentCallRouting activeICR, AgiRequest request, AgiChannel channel) throws Exception {
        try {
            setActiveICR(activeICR);
            setRequest(request);
            setChannel(channel);
            setErrorCount(0);
            setTimeoutCount(0);
            if (rowTBLPBXIVR == null) {
                TegsoftPBX.logMessage(getChannel(), Level.ERROR, "Invalid IVRID " + IVRID + " please check configuration. Exiting now.");
                return;
            }
            TegsoftPBX.setVariable(getChannel(), "CONTEXTID", rowTBLPBXIVR.getString("CONTEXTID"));
            if ("COMPLEX".equals(rowTBLPBXIVR.getString("IVRTYPE"))) {
                TegsoftPBX.logMessage(getChannel(), Level.INFO, "Complex IVR (" + IVRID + ") loaded. Executing " + rowTBLPBXIVR.getString("CLASSNAME") + " now.");
                TegsoftIVR complexIVR = (TegsoftIVR) Class.forName(rowTBLPBXIVR.getString("CLASSNAME")).getConstructor(String.class).newInstance(IVRID);
                complexIVR.execute(getActiveICR(), getRequest(), getChannel());
                return;
            }
            TegsoftPBX.logMessage(getChannel(), Level.INFO, "IVR (" + IVRID + ") loaded. Executing answer now.");
            getChannel().answer();
            String LASTERROR = "TO";
            String digits = "";
            boolean playAnnounce = true;
            int timeout = 0;
            if (rowTBLPBXIVR.getDecimal("TIMEOUT") != null) {
                timeout = rowTBLPBXIVR.getDecimal("TIMEOUT").intValue();
            }
            int inputCount = 0;
            if (rowTBLPBXIVR.getDecimal("ERRORCOUNT") != null) {
                inputCount = rowTBLPBXIVR.getDecimal("ERRORCOUNT").intValue();
            }
            while (getErrorCount() <= inputCount) {
                if (playAnnounce && NullStatus.isNotNull(rowTBLPBXIVR.getString("ANNOUNCEID"))) {
                    digits = TegsoftPBX.readInput(getChannel(), rowTBLPBXIVR.getString("ANNOUNCEID"), true, timeout, 2, -1);
                    playAnnounce = false;
                } else {
                    digits = TegsoftPBX.readInput(getChannel(), null, true, timeout, 2, -1);
                    playAnnounce = false;
                }
                if (NullStatus.isNull(digits)) {
                    TegsoftPBX.logMessage(getChannel(), Level.DEBUG, "IVR Timeout");
                    LASTERROR = "TO";
                    if (NullStatus.isNotNull(rowTBLPBXIVR.getString("TOMSGID"))) {
                        if ((getErrorCount() < inputCount)) {
                            TegsoftPBX.playBackground(getChannel(), rowTBLPBXIVR.getString("TOMSGID"));
                        }
                    }
                    if (Compare.isTrue(rowTBLPBXIVR.getString("LOOPTO"))) {
                        playAnnounce = true;
                    }
                    incTimeoutCount();
                    incErrorCount();
                } else {
                    TegsoftPBX.logMessage(getChannel(), Level.DEBUG, "Checking IVR options for " + digits);
                    for (int i = 0; i < TBLPBXIVROPT.getRowCount(); i++) {
                        DataRow rowTBLPBXIVROPT = TBLPBXIVROPT.getRow(i);
                        TegsoftPBX.logMessage(getChannel(), Level.DEBUG, "Comparing DTMF " + digits + " with IVR Option " + i + " as " + rowTBLPBXIVROPT.getString("DTMF"));
                        if (Compare.checkPattern(rowTBLPBXIVROPT.getString("DTMF"), digits)) {
                            TegsoftPBX.logMessage(getChannel(), Level.DEBUG, "Option matched executing " + rowTBLPBXIVROPT.getString("DESTTYPE") + " " + rowTBLPBXIVROPT.getString("DESTPARAM"));
                            getActiveICR().dialDESTPARAM(digits, getRequest(), getChannel(), rowTBLPBXIVROPT.getString("DESTTYPE"), rowTBLPBXIVROPT.getString("DESTPARAM"));
                            return;
                        }
                    }
                    if ("EXTENTIONS".equals(rowTBLPBXIVR.getString("DIALOPTION"))) {
                        TegsoftPBX.logMessage(getChannel(), Level.DEBUG, "EXTENTIONS allowed checking extentions for " + digits);
                        if (getActiveICR().checkExtention(digits, getRequest(), getChannel())) {
                            return;
                        }
                    }
                    if ("ALLIN".equals(rowTBLPBXIVR.getString("DIALOPTION"))) {
                        TegsoftPBX.logMessage(getChannel(), Level.DEBUG, "ALL Inbound options allowed checking all inbound options for " + digits);
                        if (getActiveICR().checkIN(digits, getRequest(), getChannel())) {
                            return;
                        }
                    }
                    TegsoftPBX.logMessage(getChannel(), Level.DEBUG, "IVR Invalid entry " + digits);
                    LASTERROR = "INV";
                    if (NullStatus.isNotNull(rowTBLPBXIVR.getString("INVALIDMSGID"))) {
                        if ((getErrorCount() < inputCount)) {
                            TegsoftPBX.playBackground(getChannel(), rowTBLPBXIVR.getString("INVALIDMSGID"));
                        }
                    }
                    if (Compare.isTrue(rowTBLPBXIVR.getString("LOOPINV"))) {
                        playAnnounce = true;
                    }
                    incErrorCount();
                }
                if (Compare.equal(digits, "-1")) {
                    TegsoftPBX.logMessage(getChannel(), Level.DEBUG, "IVR Invalid entry " + digits);
                    LASTERROR = "INV";
                    if (NullStatus.isNotNull(rowTBLPBXIVR.getString("INVALIDMSGID"))) {
                        if ((getErrorCount() < inputCount)) {
                            TegsoftPBX.playBackground(getChannel(), rowTBLPBXIVR.getString("INVALIDMSGID"));
                        }
                    }
                    if (Compare.isTrue(rowTBLPBXIVR.getString("LOOPINV"))) {
                        playAnnounce = true;
                    }
                    incErrorCount();
                }
            }
            if ("TO".equals(LASTERROR)) {
                TegsoftPBX.logMessage(getChannel(), Level.DEBUG, "Executing time-out destination");
                getActiveICR().dialDESTPARAM(digits, getRequest(), getChannel(), rowTBLPBXIVR.getString("TODESTTYPE"), rowTBLPBXIVR.getString("TODESTPARAM"));
                return;
            } else {
                TegsoftPBX.logMessage(getChannel(), Level.DEBUG, "Executing Invalid destination");
                getActiveICR().dialDESTPARAM(digits, getRequest(), getChannel(), rowTBLPBXIVR.getString("INVDESTTYPE"), rowTBLPBXIVR.getString("INVDESTPARAM"));
                return;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
