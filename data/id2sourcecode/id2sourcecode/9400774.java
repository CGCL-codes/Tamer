    public void deleteMailMessage(MailMessage toDelete) throws MessagingException {
        try {
            synchronized (this) {
                Folder inbox = this.connect4readwrite();
                if (inbox != null) {
                    this.setState(StateChangeType.MAILACCOUNT, MailBoxState.DELETING);
                    Message message = this.findMessage(inbox, toDelete);
                    if (message != null) {
                        message.setFlag(Flags.Flag.DELETED, true);
                    }
                    inbox.close(true);
                    this.store.close();
                    this.messages.remove(toDelete.hashCode());
                    this.elapsedTime = 0f;
                    this.noOfMails--;
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
