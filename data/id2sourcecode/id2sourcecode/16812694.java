    public Object getChannelObject() throws XAwareException {
        FileDriverData data = new FileDriverData();
        data.setFileName((String) m_props.get(XAwareConstants.XAWARE_FILE_NAME));
        data.setMode((String) m_props.get(XAwareConstants.BIZCOMPONENT_ATTR_REQUEST_TYPE));
        return data;
    }
