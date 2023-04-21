    public void sentEmail(Message message) throws ServiceException {
        try {
            logger.debug("Sending email");
            Transport.send(message);
        } catch (MessagingException messagingException) {
            logger.error("Error sending email.", messagingException);
            throw new ServiceException(messagingException);
        }
    }
