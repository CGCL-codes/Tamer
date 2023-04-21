    private void _invoke(MessageContext ctx, String url) throws AxisFault {
        try {
            String userName = ctx.getUsername();
            String password = ctx.getPassword();
            if ((userName != null) && (password != null)) {
                Authenticator.setDefault(new SimpleAuthenticator(userName, password));
            }
            URL urlObj = new URL(url);
            URLConnection urlc = urlObj.openConnection();
            _writeToConnection(urlc, ctx);
            _readFromConnection(urlc, ctx);
        } catch (Exception e) {
            throw AxisFault.makeFault(e);
        } finally {
            Authenticator.setDefault(null);
        }
    }
