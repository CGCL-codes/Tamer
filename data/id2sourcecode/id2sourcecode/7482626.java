    public void getChannelTypes(SnmpContextBasisFace con, AsteriskChanTypeTablePdu prev) {
        _atPdu = new AsteriskChanTypeTablePdu(con);
        _atPdu.addObserver(this);
        _atPdu.addOids(prev);
        try {
            _atPdu.send();
        } catch (java.io.IOException exc) {
            System.out.println("getChannelTypes(): IOException " + exc.getMessage());
        } catch (uk.co.westhawk.snmp.stack.PduException exc) {
            System.out.println("getChannelTypes(): PduException " + exc.getMessage());
        }
    }
