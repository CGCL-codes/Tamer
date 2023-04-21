    public int doAfterBody() throws JspException {
        BodyContent body = getBodyContent();
        if (body == null) return super.doAfterBody();
        JspWriter writer = body.getEnclosingWriter();
        String bodyString = body.getString();
        if (bodyString != null) {
            try {
                if (task != null) {
                    try {
                        HabilitationManager hm = HabilitationManagerFactory.getHabilitationManager();
                        char tmp = hm.getAccessRigths((HttpServletRequest) pageContext.getRequest(), task);
                        switch(tmp) {
                            case HabilitationManager.READ_ONLY:
                                writer.print(setReadOnly(bodyString));
                                break;
                            case HabilitationManager.ALL:
                                writer.print(bodyString);
                                return SKIP_BODY;
                            case HabilitationManager.NONE:
                                return SKIP_BODY;
                        }
                    } catch (TechniqueException e) {
                        throw new JspException(e);
                    }
                } else {
                    String mode = this.mode;
                    if ("read-only".equalsIgnoreCase(mode)) writer.print(setReadOnly(bodyString));
                    if ("read-write".equalsIgnoreCase(mode)) {
                        writer.print(bodyString);
                        return SKIP_BODY;
                    }
                    if ("none".equalsIgnoreCase(mode)) return SKIP_BODY;
                }
            } catch (IOException e) {
                throw new JspException(e);
            }
        }
        return super.doAfterBody();
    }
