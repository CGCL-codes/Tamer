    public void deleteMailMessage(List<MailMessage> toDeleteList) throws MessagingException {
        try {
            synchronized (this) {
                Folder inbox = this.connect4readwrite();
                if (inbox != null) {
                    this.setState(StateChangeType.MAILACCOUNT, MailBoxState.DELETING);
                    for (MailMessage mailMessage : toDeleteList) {
                        Message message = this.findMessage(inbox, mailMessage);
                        if (message != null) {
                            message.setFlag(Flags.Flag.DELETED, true);
                        }
                        this.messages.remove(mailMessage.hashCode());
                    }
                    inbox.close(true);
                    this.store.close();
                    this.elapsedTime = 0f;
                    this.noOfMails -= toDeleteList.size();
                    this.setState(StateChangeType.MAILBOX, MailBoxState.CHECKED);
                }
            }
        } catch (MessagingException me) {
            this.setState(StateChangeType.MAILBOX, MailBoxState.ERROR);
            throw me;
        } finally {
            if (this.store.isConnected()) {
                try {
                    this.store.close();
                } catch (MessagingException me) {
                    throw me;
                }
            }
        }
    }
