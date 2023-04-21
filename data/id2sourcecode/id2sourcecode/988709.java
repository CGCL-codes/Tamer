    @Override
    public int send(ALMailContext context) {
        try {
            ALSmtpMailContext mcontext = (ALSmtpMailContext) context;
            ALLocalMailMessage msg = createMessage(mcontext);
            if (msg == null) {
                return SEND_MSG_FAIL;
            }
            int mailSize = msg.getSize();
            if (mailSize > ALMailUtils.getMaxMailSize()) {
                return SEND_MSG_OVER_MAIL_MAX_SIZE;
            }
            if (scontext.getAuthSendFlag() == AUTH_SEND_NONE) {
                Transport.send(msg);
            } else if (scontext.getAuthSendFlag() == AUTH_SEND_POP_BEFORE_SMTP) {
                boolean success = ALPop3MailReceiver.isAuthenticatedUser(scontext.getPop3Host(), scontext.getPop3Port(), scontext.getPop3UserId(), scontext.getPop3UserPasswd(), scontext.getPop3EncryptionFlag());
                if (!success) {
                    return SEND_MSG_FAIL_POP_BEFORE_SMTP_AUTH;
                } else {
                    Thread.sleep(POP_BEFORE_SMTP_WAIT_TIME);
                }
                Transport.send(msg);
            } else if (scontext.getAuthSendFlag() == AUTH_SEND_SMTP_AUTH) {
                Transport transport = session.getTransport("smtp");
                SMTPTransport smtpt = (SMTPTransport) transport;
                smtpt.setSASLRealm("localhost");
                smtpt.connect(scontext.getSmtpHost(), scontext.getAuthSendUserId(), scontext.getAuthSendUserPassword());
                smtpt.sendMessage(msg, msg.getAllRecipients());
                smtpt.close();
            }
            ALFolder sendFolder = getALFolder();
            sendFolder.saveMail(msg, null);
        } catch (AuthenticationFailedException ex) {
            logger.error("Exception", ex);
            return SEND_MSG_FAIL_SMTP_AUTH;
        } catch (Exception ex) {
            logger.error("Exception", ex);
            return SEND_MSG_FAIL;
        } catch (Throwable e) {
            logger.error("Throwable", e);
            return SEND_MSG_FAIL;
        }
        return SEND_MSG_SUCCESS;
    }
