    @Override
    public String toString() {
        StringBuffer sb;
        sb = new StringBuffer("AgiRequest[");
        sb.append("script='").append(getScript()).append("',");
        sb.append("requestURL='").append(getRequestURL()).append("',");
        sb.append("channel='").append(getChannel()).append("',");
        sb.append("uniqueId='").append(getUniqueId()).append("',");
        sb.append("type='").append(getType()).append("',");
        sb.append("language='").append(getLanguage()).append("',");
        sb.append("callerIdNumber='").append(getCallerIdNumber()).append("',");
        sb.append("callerIdName='").append(getCallerIdName()).append("',");
        sb.append("dnid='").append(getDnid()).append("',");
        sb.append("rdnis='").append(getRdnis()).append("',");
        sb.append("context='").append(getContext()).append("',");
        sb.append("extension='").append(getExtension()).append("',");
        sb.append("priority='").append(getPriority()).append("',");
        sb.append("enhanced='").append(getEnhanced()).append("',");
        sb.append("accountCode='").append(getAccountCode()).append("',");
        sb.append("systemHashcode=").append(System.identityHashCode(this));
        sb.append("]");
        return sb.toString();
    }
