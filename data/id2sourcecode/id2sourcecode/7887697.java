    private synchronized void bridgeWithAgent(String extension, AgiRequest request, AgiChannel channel) throws Exception {
        String CAMPAIGNID = channel.getVariable("CAMPAIGNID");
        String CONTID = channel.getVariable("CONTID");
        String ORDERID = channel.getVariable("ORDERID");
        String DIALID = channel.getVariable("DIALID");
        String AGENT = null;
        new Command("UPDATE TBLCCCAMPCALLD SET DIALRESULT='ANSWER' WHERE DIALID='" + DIALID + "'").executeNonQuery(true);
        TegsoftPBX.logMessage(channel, Level.INFO, "Executing BRIDGE for " + CAMPAIGNID);
        StatusEvent targetChannel = null;
        ManagerConnection managerConnection = TegsoftPBX.createManagerConnection(PBXID);
        managerConnection.prepareChannels();
        for (int i = 0; i < managerConnection.getChannels().size(); i++) {
            StatusEvent statusEvent = managerConnection.getChannels().get(i);
            if (Compare.equal("6", statusEvent.getChannelState())) {
                if (NullStatus.isNull(statusEvent.getBridgedChannel())) {
                    String agentCAMPAIGNID = TegsoftPBX.getChannelVariable(managerConnection, statusEvent, "CAMPWAITING");
                    if (Compare.equal(CAMPAIGNID, agentCAMPAIGNID)) {
                        targetChannel = statusEvent;
                        TegsoftPBX.setChannelVariable(managerConnection, statusEvent, "CAMPWAITING", "PROGRESS");
                        AGENT = TegsoftPBX.getChannelVariable(managerConnection, statusEvent, "UID");
                        break;
                    }
                }
            }
        }
        managerConnection.disconnect();
        if (targetChannel == null) {
            Command command = new Command("SELECT COUNT(*) FROM TBLCCCAMPCALLD WHERE UNITUID={UNITUID} AND CAMPAIGNID=");
            command.bind(CAMPAIGNID);
            command.append(" AND CONTID=");
            command.bind(CONTID);
            int callCount = command.executeScalarAsDecimal().intValue();
            if (callCount == 1) {
                command = new Command("DELETE FROM TBLCCCAMPCALLD WHERE UNITUID={UNITUID} AND CAMPAIGNID=");
                command.bind(CAMPAIGNID);
                command.append(" AND CONTID=");
                command.bind(CONTID);
                command.append(" AND ORDERID=");
                command.bind(ORDERID);
                command.executeNonQuery();
                command = new Command("UPDATE TBLCCCAMPCONT SET STATUS=NULL WHERE UNITUID={UNITUID} AND CAMPAIGNID=");
                command.bind(CAMPAIGNID);
                command.append(" AND CONTID=");
                command.bind(CONTID);
                command.executeNonQuery();
            } else {
                command = new Command("SELECT MAX(ORDERID) FROM TBLCCCAMPCALLD WHERE UNITUID={UNITUID} AND CAMPAIGNID=");
                command.bind(CAMPAIGNID);
                command.append(" AND CONTID=");
                command.bind(CONTID);
                command.append("AND ORDERID<");
                command.bind(ORDERID);
                BigDecimal prevORDERID = command.executeScalarAsDecimal();
                command = new Command("DELETE FROM TBLCCCAMPCALLD WHERE UNITUID={UNITUID} AND CAMPAIGNID=");
                command.bind(CAMPAIGNID);
                command.append(" AND CONTID=");
                command.bind(CONTID);
                command.append(" AND ORDERID=");
                command.bind(ORDERID);
                command.executeNonQuery();
                DialerThread.createNextCall(CAMPAIGNID, CONTID, prevORDERID);
            }
        } else if (targetChannel != null) {
            Command command = new Command("UPDATE TBLCCCAMPCONT A SET A.AGENT=");
            command.bind(AGENT);
            command.append(", A.STATUS='PROGRESS'  WHERE A.UNITUID={UNITUID} AND CAMPAIGNID=");
            command.bind(CAMPAIGNID);
            command.append("AND CONTID=");
            command.bind(CONTID);
            command.executeNonQuery(true);
            command = new Command("UPDATE TBLCCAGENTLOG A SET A.ENDDATE={NOW}, A.MODUID=");
            command.bind(AGENT);
            command.append(",A.MODDATE={NOW} ");
            command.append("WHERE A.ENDDATE IS NULL AND A.UID=");
            command.bind(AGENT);
            command.append("AND LOGTYPE='CAMP' ");
            command.executeNonQuery(true);
            managerConnection = TegsoftPBX.createManagerConnection(PBXID);
            GetVarResponse getVarResponse = (GetVarResponse) managerConnection.sendAction(new GetVarAction(targetChannel.getChannel(), "MixMonitorCallID"), 5000);
            String MixMonitorCallID = getVarResponse.getValue();
            managerConnection.disconnect();
            String folderName = "/var/spool/asterisk/monitor/" + new SimpleDateFormat("yyyy/MM/dd").format(new Date()) + "/";
            new File(folderName).mkdirs();
            Runtime.getRuntime().exec("chown asterisk.asterisk " + folderName);
            channel.exec("MixMonitor", "/var/spool/asterisk/monitor/" + new SimpleDateFormat("yyyy/MM/dd").format(new Date()) + "/TOBE-" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + "-" + MixMonitorCallID + ".wav" + asteriskCommandSeperator + "a");
            TegsoftPBX.logMessage(channel, Level.INFO, "Changing id to->" + MixMonitorCallID + " for " + channel.getVariable("DIALID"));
            new Command("UPDATE TBLCCCAMPCALLD SET CALLID='" + MixMonitorCallID + "' WHERE DIALID='" + channel.getVariable("DIALID") + "'").executeNonQuery(true);
            TegsoftPBX.logMessage(channel, Level.INFO, "Before Bridge!! uid->" + channel.getUniqueId());
            String roomName = targetChannel.getChannel();
            if (roomName.indexOf("/") > 0) {
                roomName = roomName.substring(roomName.indexOf("/") + 1);
            }
            if (roomName.indexOf("-") > 0) {
                roomName = roomName.substring(0, roomName.indexOf("-"));
            }
            roomName = "99" + roomName;
            channel.exec("MeetMe", roomName);
            managerConnection = TegsoftPBX.createManagerConnection(PBXID);
            managerConnection.sendAction(new CommandAction("meetme kick " + roomName + " all"), 5000);
            managerConnection.disconnect();
        }
    }
