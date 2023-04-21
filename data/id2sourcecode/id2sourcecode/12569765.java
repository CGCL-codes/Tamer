    public void receiveMSG(MessageMSG message) {
        try {
            log.debug("OTP Authenticator.receiveMSG");
            String data = null;
            Blob blob = null;
            if ((state != STATE_STARTED) && (state != STATE_ID)) {
                abort(ERR_OTP_STATE);
            }
            try {
                InputStream is = message.getDataStream().getInputStream();
                int limit = is.available();
                byte buff[] = new byte[limit];
                is.read(buff);
                blob = new Blob(new String(buff));
                data = blob.getData();
            } catch (IOException x) {
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
                Blob reply = receiveIDs(data);
                try {
                    message.sendRPY(new StringOutputDataStream(reply.toString()));
                } catch (BEEPException x) {
                    throw new SASLException(x.getMessage());
                }
                return;
            }
            SessionCredential cred = null;
            cred = validateResponse(data);
            if (cred != null) {
                profile.finishListenerAuthentication(cred, channel.getSession());
                state = STATE_COMPLETE;
                if (log.isDebugEnabled()) {
                    log.debug("" + channel.getSession() + " is valid for\n" + cred.toString());
                }
                try {
                    message.sendRPY(new StringOutputDataStream(new Blob(Blob.STATUS_COMPLETE).toString()));
                    channel.setRequestHandler(null);
                } catch (BEEPException x) {
                    profile.failListenerAuthentication(channel.getSession(), authenticated);
                    abortNoThrow(x.getMessage());
                    message.getChannel().getSession().terminate(x.getMessage());
                    return;
                }
            }
        } catch (SASLException s) {
            try {
                profile.failListenerAuthentication(channel.getSession(), authenticated);
                Blob reply = new Blob(Blob.STATUS_ABORT, s.getMessage());
                message.sendRPY(new StringOutputDataStream(reply.toString()));
            } catch (BEEPException x) {
                message.getChannel().getSession().terminate(s.getMessage());
            }
        }
    }
