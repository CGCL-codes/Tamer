    private void callContact(DataRow rowTBLCCCAMPAIGN, String PBXID) throws Exception {
        int MAXCHANNEL = 0;
        Command command = new Command("SELECT COUNT(*) FROM TBLCCCAMPCONT WHERE UNITUID={UNITUID} AND STATUS='PROGRESS' AND CAMPAIGNID=");
        command.bind(rowTBLCCCAMPAIGN.getString("CAMPAIGNID"));
        int activeCallCount = command.executeScalarAsDecimal().intValue();
        if ("UNATTENDED".equals(rowTBLCCCAMPAIGN.getString("CAMPTYPE"))) {
            if (activeCallCount >= rowTBLCCCAMPAIGN.getDecimal("MAXCHANNEL").intValue()) {
                return;
            }
        } else {
            MAXCHANNEL = 0;
            final ManagerConnection managerConnection = TegsoftPBX.createManagerConnection(PBXID);
            managerConnection.prepareChannels();
            for (int i = 0; i < managerConnection.getChannels().size(); i++) {
                StatusEvent statusEvent = managerConnection.getChannels().get(i);
                if (Compare.equal("6", statusEvent.getChannelState())) {
                    if (NullStatus.isNull(statusEvent.getBridgedChannel())) {
                        String agentCAMPAIGNID = TegsoftPBX.getChannelVariable(managerConnection, statusEvent, "CAMPWAITING");
                        if (Compare.equal(rowTBLCCCAMPAIGN.getString("CAMPAIGNID"), agentCAMPAIGNID)) {
                            MAXCHANNEL++;
                        }
                    }
                }
            }
            managerConnection.disconnect();
            if (activeCallCount >= MAXCHANNEL) {
                return;
            }
        }
        command = new Command("SELECT * FROM TBLCCCAMPCALL WHERE UNITUID={UNITUID} AND CAMPAIGNID=");
        command.bind(rowTBLCCCAMPAIGN.getString("CAMPAIGNID"));
        command.append("ORDER BY ORDERID");
        TBLCCCAMPCALL.fill(command);
        if (TBLCCCAMPCALL.getRowCount() == 0) {
            return;
        }
        command = new Command("SELECT * FROM TBLCCCAMPCALLD WHERE UNITUID={UNITUID} AND STATUS='IVRSCHEDULED' AND CAMPAIGNID=");
        command.bind(rowTBLCCCAMPAIGN.getString("CAMPAIGNID"));
        command.append("AND CALLDATE<{NOW} ");
        TBLCCCAMPCALLD.fill(command);
        for (int i = 0; i < TBLCCCAMPCALLD.getRowCount(); i++) {
            DataRow rowTBLCCCAMPCALLD = TBLCCCAMPCALLD.getRow(i);
            command = new Command("SELECT * FROM TBLCCCAMPCALL WHERE UNITUID={UNITUID} AND CAMPAIGNID=");
            command.bind(rowTBLCCCAMPAIGN.getString("CAMPAIGNID"));
            command.append("AND ORDERID=");
            command.bind(rowTBLCCCAMPCALLD.getString("ORDERID"));
            command.append("ORDER BY ORDERID");
            TBLCCCAMPCALL.fill(command);
            String PHONENO = TBLCCCAMPCALL.getRow(0).getString("PHONENO");
            rowTBLCCCAMPCALLD.setTimestamp("CALLDATE", DateUtil.now());
            rowTBLCCCAMPCALLD.setString("STATUS", "PROGRESS");
            TBLCCCAMPCALLD.save();
            command = new Command("SELECT A.*,B." + PHONENO + " AS PHONENO FROM TBLCCCAMPCONT A,TBLCRMCONTACTS B WHERE ");
            command.append("A.UNITUID=B.UNITUID AND A.CONTID=B.CONTID ");
            command.append("AND A.UNITUID={UNITUID} AND A.CAMPAIGNID=");
            command.bind(rowTBLCCCAMPAIGN.getString("CAMPAIGNID"));
            command.append("AND A.CONTID=");
            command.bind(rowTBLCCCAMPCALLD.getString("CONTID"));
            command.append("ORDER BY A.ORDERID");
            TBLCCCAMPCONT.fill(command);
            if (TBLCCCAMPCONT.getRowCount() > 0) {
                DataRow rowTBLCCCAMPCONT = TBLCCCAMPCONT.getRow(0);
                rowTBLCCCAMPCONT.setString("STATUS", "PROGRESS");
                TBLCCCAMPCONT.save();
                if (NullStatus.isNull(rowTBLCCCAMPCONT.getString("PHONENO"))) {
                    rowTBLCCCAMPCALLD.setString("STATUS", "CLOSED");
                    rowTBLCCCAMPCALLD.setString("DIALRESULT", "NOTCON");
                    TBLCCCAMPCALLD.save();
                    if (!DialerThread.createNextCall(rowTBLCCCAMPCALLD.getString("CAMPAIGNID"), rowTBLCCCAMPCALLD.getString("CONTID"), rowTBLCCCAMPCALLD.getDecimal("ORDERID"))) {
                        continue;
                    }
                }
                new DialerThread(rowTBLCCCAMPCALLD.getString("CAMPAIGNID"), rowTBLCCCAMPCALLD.getString("CONTID"), rowTBLCCCAMPCALLD.getDecimal("ORDERID"), PBXID).start();
                command = new Command("SELECT COUNT(*) FROM TBLCCCAMPCONT WHERE UNITUID={UNITUID} AND STATUS='PROGRESS' AND CAMPAIGNID=");
                command.bind(rowTBLCCCAMPAIGN.getString("CAMPAIGNID"));
                activeCallCount = command.executeScalarAsDecimal().intValue();
                if ("UNATTENDED".equals(rowTBLCCCAMPAIGN.getString("CAMPTYPE"))) {
                    if (activeCallCount >= rowTBLCCCAMPAIGN.getDecimal("MAXCHANNEL").intValue()) {
                        return;
                    }
                } else {
                    if (activeCallCount >= MAXCHANNEL) {
                        return;
                    }
                }
            } else {
                rowTBLCCCAMPCALLD.setString("STATUS", "CLOSED");
                rowTBLCCCAMPCALLD.setString("DIALRESULT", "NOTCON");
                TBLCCCAMPCALLD.save();
            }
        }
        String PHONENO = TBLCCCAMPCALL.getRow(0).getString("PHONENO");
        command = new Command("SELECT A.*,B." + PHONENO + " AS PHONENO FROM TBLCCCAMPCONT A,TBLCRMCONTACTS B WHERE ");
        command.append("A.UNITUID=B.UNITUID AND A.CONTID=B.CONTID ");
        command.append("AND A.UNITUID={UNITUID} AND A.STATUS IS NULL AND A.CAMPAIGNID=");
        command.bind(rowTBLCCCAMPAIGN.getString("CAMPAIGNID"));
        command.append("ORDER BY A.ORDERID");
        TBLCCCAMPCONT.fill(command);
        for (int i = 0; i < TBLCCCAMPCONT.getRowCount(); i++) {
            DataRow rowTBLCCCAMPCONT = TBLCCCAMPCONT.getRow(i);
            rowTBLCCCAMPCONT.setString("STATUS", "PROGRESS");
            TBLCCCAMPCONT.save();
            DataRow rowTBLCCCAMPCALLD = TBLCCCAMPCALLD.addNewDataRow();
            rowTBLCCCAMPCALLD.setString("CAMPAIGNID", rowTBLCCCAMPAIGN.getString("CAMPAIGNID"));
            rowTBLCCCAMPCALLD.setString("CONTID", rowTBLCCCAMPCONT.getString("CONTID"));
            rowTBLCCCAMPCALLD.setDecimal("ORDERID", TBLCCCAMPCALL.getRow(0).getDecimal("ORDERID"));
            rowTBLCCCAMPCALLD.setTimestamp("CALLDATE", DateUtil.now());
            rowTBLCCCAMPCALLD.setString("STATUS", "PROGRESS");
            TBLCCCAMPCALLD.save();
            if (NullStatus.isNull(rowTBLCCCAMPCONT.getString("PHONENO"))) {
                rowTBLCCCAMPCALLD.setString("STATUS", "CLOSED");
                rowTBLCCCAMPCALLD.setString("DIALRESULT", "NOTCON");
                TBLCCCAMPCALLD.save();
                if (!DialerThread.createNextCall(rowTBLCCCAMPCALLD.getString("CAMPAIGNID"), rowTBLCCCAMPCALLD.getString("CONTID"), rowTBLCCCAMPCALLD.getDecimal("ORDERID"))) {
                    continue;
                }
            }
            new DialerThread(rowTBLCCCAMPCALLD.getString("CAMPAIGNID"), rowTBLCCCAMPCALLD.getString("CONTID"), rowTBLCCCAMPCALLD.getDecimal("ORDERID"), PBXID).start();
            command = new Command("SELECT COUNT(*) FROM TBLCCCAMPCONT WHERE UNITUID={UNITUID} AND STATUS='PROGRESS' AND CAMPAIGNID=");
            command.bind(rowTBLCCCAMPAIGN.getString("CAMPAIGNID"));
            activeCallCount = command.executeScalarAsDecimal().intValue();
            if ("UNATTENDED".equals(rowTBLCCCAMPAIGN.getString("CAMPTYPE"))) {
                if (activeCallCount >= rowTBLCCCAMPAIGN.getDecimal("MAXCHANNEL").intValue()) {
                    return;
                }
            } else {
                if (activeCallCount >= MAXCHANNEL) {
                    return;
                }
            }
        }
    }
