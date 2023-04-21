    public void writeNoKeyData(EntityId readerId, ParameterList parms, SerializedData user_data) {
        _readerId = readerId;
        _parms = parms;
        _user_data = user_data;
        _submessageKind = NOKEY_DATA.value;
        writeSubmessage();
    }
