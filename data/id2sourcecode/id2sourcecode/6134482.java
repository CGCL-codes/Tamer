    public boolean updateParticipant(int participantId, String fullname, String email, String username, String password, int[] participantGroupIds, String location, String city, String state, int countryId, String msnMessager, String yahooMessager, String aolMessager) {
        String participantGroupProfile = "";
        String attribution = "";
        String participantGroupQueries[] = new String[participantGroupIds.length + 1];
        if (participantGroupIds != null) {
            for (int i = 0; i < participantGroupIds.length; i++) {
                participantGroupProfile = participantGroupProfile + participantGroupIds[i] + ":";
            }
            attribution += "participantGroupIds = '" + participantGroupProfile + "', ";
        }
        if (!fullname.equals("")) {
            attribution += "fullname = '" + fullname + "', ";
        }
        if (!email.equals("")) {
            attribution += "email = '" + email + "', ";
        }
        if (!username.equals("")) {
            attribution += "username = '" + username + "', ";
        }
        if (!password.equals("")) {
            attribution += "password = '" + password + "', ";
        }
        if (!location.equals("")) {
            attribution += "location = '" + location + "', ";
        }
        if (!city.equals("")) {
            attribution += "city = '" + city + "', ";
        }
        if (!state.equals("")) {
            attribution += "state = '" + state + "', ";
        }
        if (countryId != 0) {
            attribution += "countryId = '" + countryId + "', ";
        }
        if (!msnMessager.equals("")) {
            attribution += "msnMessager = '" + msnMessager + "', ";
        }
        if (!yahooMessager.equals("")) {
            attribution += "yahooMessager = '" + yahooMessager + "', ";
        }
        if (!aolMessager.equals("")) {
            attribution += "aolMessager" + aolMessager + "', ";
        }
        byte[] b = null;
        try {
            b = MD5Crypt.digest(password.getBytes(), "md5");
            password = MD5Crypt.byteArrayToHexString(b);
        } catch (NoSuchAlgorithmException e) {
            return false;
        }
        participantGroupProfile = participantGroupProfile.substring(0, participantGroupProfile.length() - 1);
        _query = "UPDATE Participant, ParticipantProfile SET" + attribution + " WHERE participantId = " + participantId + " Participant.profileId = ParticipantProfile.profileId";
        if (this.databaseManager.queryInTransaction(_query, false)) {
            participantGroupQueries[0] = "DELETE FROM ParticipantGroup WHERE participantId = " + participantId;
            for (int i = 1; i < participantGroupIds.length; i++) {
                participantGroupQueries[i] = "INSERT INTO ParticipantGroup VALUES (" + participantGroupIds[i] + ", " + participantId + ")";
            }
            return this.databaseManager.queryInTransaction(participantGroupQueries, true);
        } else {
            return false;
        }
    }
