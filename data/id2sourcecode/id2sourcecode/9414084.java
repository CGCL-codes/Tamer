        @Override
        public void run() {
            int trials = 0;
            try {
                while (true) {
                    String[] read = { "Account", "Model" };
                    String[] write = { "Trade", "History" };
                    worker.log("locking");
                    worker.db.lock(read, write);
                    worker.log("locked");
                    trials++;
                    String queryTrades = "select tradeId, accountName , modelName , Trade.amount from Account, Trade, Model where accountName ='" + name + "' and Trade.price =0 and Trade.accountId=Account.accountId and Trade.modelId=Model.modelId";
                    Pair<Statement, ResultSet> result = worker.db.executeQuery(queryTrades);
                    while (result.second.next()) {
                        trials = 0;
                        String tradeId = result.second.getString("tradeId");
                        String accountName = result.second.getString("accountName");
                        String modelName = result.second.getString("modelName");
                        String amount = result.second.getString("amount");
                        double price = (1.0 + Math.random());
                        String update = "update Trade set price = " + price + " where tradeId = " + tradeId;
                        worker.log(accountName + ": " + modelName + " traded " + amount + " at: " + price);
                        worker.db.executeUpdate(update);
                        String flush = "insert into History select * from Trade where tradeId = " + tradeId;
                        worker.db.executeUpdate(flush);
                        String delete = "delete from Trade where tradeId = " + tradeId;
                        worker.db.executeUpdate(delete);
                        trades++;
                    }
                    worker.db.unlock();
                    if (trials > 100) return;
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                    }
                }
            } catch (RequestException e) {
                worker.log(e.getMessage());
                fail(e.getMessage());
                e.printStackTrace();
            } catch (SQLException e) {
                worker.log(e.getMessage());
                fail(e.getMessage());
                e.printStackTrace();
            } finally {
                try {
                    worker.db.unlock();
                } catch (RequestException e) {
                    e.printStackTrace();
                }
                try {
                    worker.db.disconnect();
                } catch (RequestExceptionConnection e) {
                    e.printStackTrace();
                }
            }
        }
