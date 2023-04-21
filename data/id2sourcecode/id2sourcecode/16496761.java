    public int doStartTag() throws JspException {
        counter = 0;
        try {
            channels = (new ChannelDAO()).getChannels(parentPath);
        } catch (Exception ex) {
        }
        if (channels.length <= 0) {
            return SKIP_BODY;
        } else {
            pageContext.setAttribute(id, channels[counter++]);
        }
        return EVAL_BODY_TAG;
    }
