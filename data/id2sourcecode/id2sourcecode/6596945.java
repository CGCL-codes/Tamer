    private synchronized void processRemoteMessage(final Message message) {
        logger.info("processing a remote message");
        Engine engine = EngineFactory.getEngine(EngineFactory.DEFAULT);
        if (message.getChannel() == MessageChannel.ACCOUNT) {
            final Account account = (Account) message.getObject(MessageProperty.ACCOUNT);
            switch(message.getEvent()) {
                case ACCOUNT_ADD:
                case ACCOUNT_REMOVE:
                    engine.refreshAccount(account);
                    message.setObject(MessageProperty.ACCOUNT, engine.getStoredObjectByUuid(account.getUuid()));
                    engine.refreshAccount(account.getParent());
                    break;
                case ACCOUNT_MODIFY:
                case ACCOUNT_SECURITY_ADD:
                case ACCOUNT_SECURITY_REMOVE:
                case ACCOUNT_VISIBILITY_CHANGE:
                    engine.refreshAccount(account);
                    message.setObject(MessageProperty.ACCOUNT, engine.getStoredObjectByUuid(account.getUuid()));
                    break;
                default:
                    break;
            }
        }
        if (message.getChannel() == MessageChannel.BUDGET) {
            final Budget budget = (Budget) message.getObject(MessageProperty.BUDGET);
            switch(message.getEvent()) {
                case BUDGET_ADD:
                case BUDGET_UPDATE:
                case BUDGET_REMOVE:
                case BUDGET_GOAL_UPDATE:
                    engine.refreshBudget(budget);
                    message.setObject(MessageProperty.BUDGET, engine.getStoredObjectByUuid(budget.getUuid()));
                    break;
                default:
                    break;
            }
        }
        if (message.getChannel() == MessageChannel.COMMODITY) {
            switch(message.getEvent()) {
                case CURRENCY_MODIFY:
                case COMMODITY_HISTORY_ADD:
                case COMMODITY_HISTORY_REMOVE:
                    final CommodityNode node = (CommodityNode) message.getObject(MessageProperty.COMMODITY);
                    engine.refreshCommodity(node);
                    message.setObject(MessageProperty.COMMODITY, engine.getStoredObjectByUuid(node.getUuid()));
                    break;
                case EXCHANGERATE_ADD:
                case EXCHANGERATE_REMOVE:
                    final ExchangeRate rate = (ExchangeRate) message.getObject(MessageProperty.EXCHANGERATE);
                    engine.refreshExchangeRate(rate);
                    message.setObject(MessageProperty.EXCHANGERATE, engine.getStoredObjectByUuid(rate.getUuid()));
                    break;
                default:
                    break;
            }
        }
        if (message.getChannel() == MessageChannel.REMINDER) {
            switch(message.getEvent()) {
                case REMINDER_ADD:
                case REMINDER_REMOVE:
                    final Reminder reminder = (Reminder) message.getObject(MessageProperty.REMINDER);
                    engine.refreshReminder(reminder);
                    message.setObject(MessageProperty.REMINDER, engine.getStoredObjectByUuid(reminder.getUuid()));
                    break;
                default:
                    break;
            }
        }
        if (message.getChannel() == MessageChannel.TRANSACTION) {
            switch(message.getEvent()) {
                case TRANSACTION_ADD:
                case TRANSACTION_REMOVE:
                    final Account account = (Account) message.getObject(MessageProperty.ACCOUNT);
                    engine.refreshAccount(account);
                    message.setObject(MessageProperty.ACCOUNT, engine.getStoredObjectByUuid(account.getUuid()));
                    final Transaction transaction = (Transaction) message.getObject(MessageProperty.TRANSACTION);
                    engine.refreshTransaction(transaction);
                    message.setObject(MessageProperty.TRANSACTION, engine.getStoredObjectByUuid(transaction.getUuid()));
                    break;
                default:
                    break;
            }
        }
        message.setRemote(true);
        logger.info("fire remote message");
        MessageBus.getInstance().fireEvent(message);
    }
