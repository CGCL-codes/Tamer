    public void receiveMSG(MessageMSG message) {
        try {
            log.debug("Anonymous Authenticator.receiveMSG");
            String data = null;
            Blob blob = null;
            if (state != STATE_STARTED) {
                abort(ERR_ANON_STATE);
            }
            try {
                InputStream is = message.getDataStream().getInputStream();
                int limit = is.available();
                byte buff[] = new byte[limit];
                is.read(buff);
                blob = new Blob(new String(buff));
                data = blob.getData();
            } catch (IOException x) {
                log.error("", x);
                abort(x.getMessage());
            }
            if (log.isDebugEnabled()) {
                log.debug("MSG DATA=>" + data);
            }
            String status = blob.getStatus();
            if ((status != null) && status.equals(SASLProfile.SASL_STATUS_ABORT)) {
                abort(ERR_PEER_ABORTED);
            }
            if (state == STATE_STARTED) {
                try {
                    Blob reply = receiveID(data);
                    message.sendRPY(new StringOutputDataStream(reply.toString()));
                } catch (BEEPException x) {
                    abort(x.getMessage());
                }
                profile.finishListenerAuthentication(new SessionCredential(credential), channel.getSession());
            }
        } catch (SASLException s) {
            try {
                Blob reply = new Blob(Blob.STATUS_ABORT, s.getMessage());
                message.sendRPY(new StringOutputDataStream(reply.toString()));
            } catch (BEEPException t) {
                message.getChannel().getSession().terminate(t.getMessage());
            }
        }
    }
