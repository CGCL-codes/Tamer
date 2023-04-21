    @Override
    protected AuthenticationResult doExecute() throws InterruptedException, RestCommandException {
        pDigestURL = false;
        final AuthenticationResult first = super.doExecute();
        String respStr;
        try {
            final String password = getParam(KEY_PASSWORD);
            final String nonce = first.getNonce();
            respStr = EvdbUtils.digest(nonce, password);
        } catch (final UnsupportedEncodingException e) {
            respStr = "";
        }
        setParam(KEY_NONCE, first.getNonce());
        setParam(KEY_RESPONSE, respStr);
        pDigestURL = true;
        final AuthenticationResult result = super.doExecute();
        result.setNonce(first.getNonce());
        setParam(KEY_NONCE, null);
        setParam(KEY_RESPONSE, null);
        return result;
    }
