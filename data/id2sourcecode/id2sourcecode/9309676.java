    @Listener("/service/notification/status")
    public void processNotification(final ServerSession remote, final ServerMessage.Mutable message) {
        final Map<String, Object> input = message.getDataAsMap();
        final Map<String, Object> output = new HashMap<String, Object>();
        UserAccount userAccount;
        try {
            userAccount = getByUsername(getUserPrincipalUsername());
            if (userAccount != null) {
                final Long totalNot = getNotificationDao().retrieveTotalNotificationStatus(userAccount.getAccount());
                log.debug("totalNot " + totalNot);
                final Long totalNewNot = getNotificationDao().retrieveTotalNotReadedNotificationStatus(userAccount.getAccount());
                log.debug("totalNewNot " + totalNewNot);
                output.put("totalNot", totalNot);
                output.put("totalNewNot", totalNewNot);
                log.debug(totalNewNot + " NEW of " + totalNot + " total not");
            } else {
                output.put("totalNot", 0);
                output.put("totalNewNot", 0);
            }
        } catch (EnMeNoResultsFoundException e) {
            output.put("totalNot", 0);
            output.put("totalNewNot", 0);
            log.fatal("cometd: username invalid");
        }
        remote.deliver(getServerSession(), message.getChannel(), output, null);
    }
