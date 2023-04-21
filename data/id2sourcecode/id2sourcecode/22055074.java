    public void setContext(NameValue contextVariable) throws AoException {
        log.debug("Enter AoSessionImpl::setContext()");
        String key = contextVariable.valName;
        if (this.context.containsKey(key)) {
            this.context.remove(key);
        }
        if ("PASSWORD".equals(key)) {
            try {
                MessageDigest md = MessageDigest.getInstance("MD5");
                contextVariable.value.u.stringVal(new String(md.digest(contextVariable.value.u.stringVal().getBytes())));
            } catch (NoSuchAlgorithmException e) {
                log.error(e.getMessage());
                throw new AoException(ErrorCode.AO_IMPLEMENTATION_PROBLEM, SeverityFlag.ERROR, 0, "AoSessionImpl::setContext()");
            }
        }
        this.context.put(key, contextVariable);
        log.debug("Exit AoSessionImpl::setContext()");
    }
