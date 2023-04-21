    protected static void login(Connection con, String[] information) throws SQLException, VerificationException {
        Player logined = null;
        if (!Verification.verifyLoginName(information[0])) {
            throw new VerificationException("Failure on loginname! " + information[0]);
        }
        if (!Verification.verifyPassword(information[1])) {
            throw new VerificationException("Failure on password! " + information[1]);
        }
        if (Database.verifyLogin(information[0], information[1])) {
            logined = new Player(con, information[0]);
            if (PlayerData.logIn(logined, con)) {
                con.setPlayer(logined);
                Protocol.confirmLogin(logined.getConnection(), logined.getLoginName());
                Protocol.setInGameName(logined);
                logined.sendMyContactStatusToMyContacts();
                Protocol.sendMuteBanData(logined);
                if (information.length > 3) {
                    logined.setPlaying(Boolean.valueOf(information[2]));
                    logined.setPlayingOnChannel(ChannelData.getChannel(information[3]));
                }
            }
        } else {
            Protocol.wrongLogin(con);
        }
    }
