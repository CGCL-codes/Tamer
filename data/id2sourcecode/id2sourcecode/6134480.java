    public boolean insertParticipant(String fullname, String email, String username, String password, int participantGroupIds[], String location, String city, String state, int countryId, boolean enabled, String msnMessager, String yahooMessager, String aolMessager) {
        String participantGroupProfile = "";
        String participantGroupQueries[] = new String[participantGroupIds.length];
        char participantEnabled = (enabled) ? 'Y' : 'N';
        participantGroupProfile = participantGroupProfile.substring(0, participantGroupProfile.length() - 1);
        byte[] b = null;
        try {
            b = MD5Crypt.digest(password.getBytes(), "md5");
        } catch (NoSuchAlgorithmException e) {
            return false;
        }
        password = MD5Crypt.byteArrayToHexString(b);
        _query = "INSERT INTO Participant VALUES ('', '" + fullname + "', '" + email + "', '" + username + "', password('" + password + "'))";
        if (this.databaseManager.queryInTransaction(_query, false)) {
            int participantId = this.databaseManager.getLastInsertedId();
            if (participantId == -1) {
                this.databaseManager.rollback();
                return false;
            }
            _query = "INSERT INTO ParticipantProfile VALUES (" + participantId + ", '" + location + "', '" + city + "', '" + state + "', " + countryId + ", '" + participantEnabled + "', '" + participantGroupProfile + "', '" + msnMessager + "', '" + yahooMessager + "', '" + aolMessager + "')";
            if (this.databaseManager.queryInTransaction(_query, false)) {
                for (int i = 0; i < participantGroupIds.length; i++) {
                    participantGroupQueries[i] = "INSERT INTO ParticipantGroup VALUES (" + participantGroupIds[i] + ", " + participantId + ")";
                }
                return this.databaseManager.queryInTransaction(participantGroupQueries, true);
            } else {
                return false;
            }
        }
        return false;
    }
