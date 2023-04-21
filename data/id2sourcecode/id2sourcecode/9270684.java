    public void launch(final String ballotLocation, String logDir, String logFilename, boolean debug, final String vvpat, final int vvpatWidth, final int vvpatHeight, final int printableWidth, final int printableHeight, final EvilObserver evilObserver) {
        File baldir;
        try {
            baldir = File.createTempFile("ballot", "");
            baldir.delete();
            baldir.mkdirs();
            Driver.unzip(ballotLocation, baldir.getAbsolutePath());
            Driver.deleteRecursivelyOnExit(baldir.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        System.out.println(baldir.getAbsolutePath());
        File logdir = new File(logDir);
        File logfile = new File(logdir, logFilename);
        if (!baldir.isDirectory()) {
            _view.statusMessage("Supplied 'ballot location' is not a directory.", "Please make sure that you select a directory which contains a ballot configuration file and media directory. Do not select a file.");
            return;
        }
        if (!Arrays.asList(baldir.list()).contains("ballotbox.cfg")) {
            _view.statusMessage("Supplied 'ballot location' does not contain the file 'ballotbox.cfg'", "Please specify a valid ballot.zip or ballot directory.");
            return;
        }
        if (!logdir.isDirectory()) {
            _view.statusMessage("Supplied 'log directory' is not a directory.", "Please make sure that you select a directory\nfor 'log directory' field. Do not select a file.");
            return;
        }
        if (logFilename.equals("")) {
            _view.statusMessage("Log Filename blank.", "Please specify a log filename.");
            return;
        }
        if (logfile.exists()) {
            int i = 2;
            String startname = logfile.getName();
            while (logfile.exists()) logfile = new File(startname + "-" + i++);
            if (!_view.askQuestion("Supplied 'log file' exists", "If you choose to continue, event data will be recorded to the file: " + logfile.getName())) return;
        }
        DataLogger.init(logfile);
        save(ballotLocation, logDir, logFilename);
        _voteBox = null;
        System.gc();
        _voteBox = new Driver(baldir.getAbsolutePath(), new AWTViewFactory(debug, false), false);
        final Driver vbcopy = _voteBox;
        _view.setRunning(true);
        new Thread(new Runnable() {

            public void run() {
                final IAuditoriumParams constants = new IAuditoriumParams() {

                    public boolean getAllowUIScaling() {
                        return true;
                    }

                    public boolean getUseWindowedView() {
                        return true;
                    }

                    public String getBroadcastAddress() {
                        return null;
                    }

                    public boolean getCastBallotEncryptionEnabled() {
                        return false;
                    }

                    public String getChallengeBallotFile() {
                        return null;
                    }

                    public int getChallengePort() {
                        return 0;
                    }

                    public int getDefaultSerialNumber() {
                        return 0;
                    }

                    public int getDiscoverPort() {
                        return 0;
                    }

                    public int getDiscoverReplyPort() {
                        return 0;
                    }

                    public int getDiscoverReplyTimeout() {
                        return 0;
                    }

                    public int getDiscoverTimeout() {
                        return 0;
                    }

                    public String getEloTouchScreenDevice() {
                        return null;
                    }

                    public int getHttpPort() {
                        return 0;
                    }

                    public int getJoinTimeout() {
                        return 0;
                    }

                    public IKeyStore getKeyStore() {
                        return null;
                    }

                    public int getListenPort() {
                        return 0;
                    }

                    public String getLogLocation() {
                        return null;
                    }

                    public int getPaperHeightForVVPAT() {
                        return vvpatHeight;
                    }

                    public int getPaperWidthForVVPAT() {
                        return vvpatWidth;
                    }

                    public int getPrintableHeightForVVPAT() {
                        return printableHeight;
                    }

                    public int getPrintableWidthForVVPAT() {
                        return printableWidth;
                    }

                    public String getPrinterForVVPAT() {
                        return vvpat;
                    }

                    public String getReportAddress() {
                        return null;
                    }

                    public String getRuleFile() {
                        return null;
                    }

                    public boolean getUseCommitChallengeModel() {
                        return false;
                    }

                    public boolean getUseEloTouchScreen() {
                        return false;
                    }

                    public int getViewRestartTimeout() {
                        return 1;
                    }

                    public boolean getEnableNIZKs() {
                        return false;
                    }

                    public boolean getUsePiecemealEncryption() {
                        return false;
                    }

                    public boolean getUseSimpleTallyView() {
                        return false;
                    }

                    public boolean getUseTableTallyView() {
                        return false;
                    }
                };
                vbcopy.registerForReview(evilObserver);
                vbcopy.run(new Observer() {

                    ListExpression _lastSeenBallot = null;

                    public void update(Observable o, Object arg) {
                        Object[] obj = (Object[]) arg;
                        if (!((Boolean) obj[0])) return;
                        ListExpression ballot = (ListExpression) obj[1];
                        boolean reject = !ballot.toString().equals("" + _lastSeenBallot);
                        try {
                            if (reject) {
                                if (_lastSeenBallot != null) Driver.printBallotRejected(constants, new File(ballotLocation));
                                Driver.printCommittedBallot(constants, ballot, new File(ballotLocation));
                            }
                        } catch (Exception e) {
                        } finally {
                            _lastSeenBallot = ballot;
                        }
                    }
                }, new Observer() {

                    public void update(Observable o, Object arg) {
                        ASExpression ballot = (ASExpression) ((Object[]) arg)[0];
                        System.out.println("Preparing to dump ballot:\n\t" + ballot);
                        DataLogger.DumpBallot(ballot);
                        Driver.printBallotAccepted(constants, new File(ballotLocation));
                        vbcopy.getView().nextPage();
                    }
                });
                _view.setRunning(true);
            }
        }).start();
    }
