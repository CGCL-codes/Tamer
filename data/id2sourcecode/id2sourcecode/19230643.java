    public Object createChannelObject() throws XAwareException {
        data = new FileDriverData();
        data = (FileDriverData) this.m_channelSpecification.getChannelObject();
        data.setFileName(this.substitute(data.getFileName(), this.getRootElement(), this.m_context));
        data.setMode(this.substitute(data.getMode(), this.getRootElement(), this.m_context));
        return data;
    }
