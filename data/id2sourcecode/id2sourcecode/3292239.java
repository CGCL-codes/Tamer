    public void writeData(EntityId readerId, KeyHashPrefix prefix, KeyHashSuffix suffix, StatusInfo status, ParameterList parms, SerializedData user_data) {
        _readerId = readerId;
        _prefix = prefix;
        _suffix = suffix;
        _status = status;
        _parms = parms;
        _user_data = user_data;
        _submessageKind = DATA.value;
        writeSubmessage();
    }
