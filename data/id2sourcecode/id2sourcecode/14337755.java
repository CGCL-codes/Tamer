    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof CommunicationType)) return false;
        CommunicationType other = (CommunicationType) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && ((this.channelCode == null && other.getChannelCode() == null) || (this.channelCode != null && this.channelCode.equals(other.getChannelCode()))) && ((this.channel == null && other.getChannel() == null) || (this.channel != null && this.channel.equals(other.getChannel()))) && ((this.value == null && other.getValue() == null) || (this.value != null && this.value.equals(other.getValue()))) && this.hjid == other.getHjid();
        __equalsCalc = null;
        return _equals;
    }
