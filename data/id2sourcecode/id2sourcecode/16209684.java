    @Override
    public String getPrintString(ObjectIdentifierValue oid) {
        MibType type = oid.getSymbol().getType();
        if (type instanceof SnmpObjectType) {
            SnmpObjectType objType = (SnmpObjectType) type;
            SnmpAccess access = objType.getAccess();
            if (access == SnmpAccess.READ_ONLY) {
                return "read-only";
            } else if (access == SnmpAccess.READ_WRITE) {
                return "read-write";
            } else if (access == SnmpAccess.READ_CREATE) {
                return "read-create";
            } else if (access == SnmpAccess.WRITE_ONLY) {
                return "write-only";
            } else if (access == SnmpAccess.NOT_IMPLEMENTED) {
                return "not-implemented";
            } else if (access == SnmpAccess.NOT_ACCESSIBLE) {
                return "not-accessible";
            } else if (access == SnmpAccess.ACCESSIBLE_FOR_NOTIFY) {
                return "accessible-for-notify";
            }
        }
        return "";
    }
