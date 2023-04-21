    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Function)) return false;
        Function other = (Function) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && ((this.functionName == null && other.getFunctionName() == null) || (this.functionName != null && this.functionName.equals(other.getFunctionName()))) && this.read == other.isRead() && this.write == other.isWrite();
        __equalsCalc = null;
        return _equals;
    }
