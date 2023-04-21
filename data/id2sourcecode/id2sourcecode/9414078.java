        public void run() {
            for (int i = 0; i < trades; i++) {
                try {
                    Thread.sleep(Math.round(Math.random() * 10));
                } catch (InterruptedException e) {
                }
                try {
                    String[] read = { "Account", "Model" };
                    String[] write = { "Trade" };
                    worker.log("locking");
                    worker.db.lock(read, write);
                    worker.log("locked");
                    double gearing = Math.random();
                    double modelAmount = 0;
                    String selectModel = "select amount from Model " + "where modelId = " + modelId;
                    Pair<Statement, ResultSet> result = worker.db.executeQuery(selectModel);
                    while (result.second.next()) {
                        modelAmount = result.second.getDouble("amount");
                    }
                    worker.db.close(result);
                    String insert = "insert into Trade (accountId, modelId, amount, price) values ";
                    String selectAccounts = "select accountId, amount from Account " + "where modelId = " + modelId;
                    result = worker.db.executeQuery(selectAccounts);
                    boolean first = true;
                    boolean traded = false;
                    while (result.second.next()) {
                        traded = true;
                        if (first) first = false; else insert += ",";
                        String accountId = result.second.getString("accountId");
                        double accountAmount = result.second.getDouble("amount");
                        insert += "(" + accountId + ", " + modelId + ", " + accountAmount / modelAmount * gearing + ", 0)";
                    }
                    if (traded) worker.db.executeUpdate(insert);
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
                    }
                }
            }
            try {
                worker.disconnect();
            } catch (RequestExceptionConnection e) {
                e.printStackTrace();
            }
        }
