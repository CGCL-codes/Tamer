    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getChannelCode() != null) {
            _hashCode += getChannelCode().hashCode();
        }
        if (getChannel() != null) {
            _hashCode += getChannel().hashCode();
        }
        if (getValue() != null) {
            _hashCode += getValue().hashCode();
        }
        _hashCode += new Long(getHjid()).hashCode();
        __hashCodeCalc = false;
        return _hashCode;
    }
