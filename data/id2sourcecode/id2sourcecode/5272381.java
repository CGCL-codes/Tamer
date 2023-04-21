    public void generate(int SEsize, int type) throws Exception {
        this.createDatabase();
        int[] quotas = new int[QuotaScheduler.NUMBER_OF_USERS];
        int quota = SEsize / QuotaScheduler.NUMBER_OF_USERS;
        int newQuota = quota + (quota * 70) / 100;
        int diffQuota = quota - (quota * 30) / 100;
        int id = 1;
        int count = 0;
        int dataId = 1;
        try {
            while (true) {
                random = new Random(System.nanoTime());
                int userId = 1 + random.nextInt(QuotaScheduler.NUMBER_OF_USERS);
                int tasks = MIN_TASKS + random.nextInt(MAX_TASKS - MIN_TASKS);
                int submitTime = 0 + random.nextInt(90000);
                int dataSize = MIN_DATA_SIZE + random.nextInt(MAX_DATA_SIZE - MIN_DATA_SIZE);
                taskfor: for (int i = 0; i < tasks; i++) {
                    int userQuota = quotas[userId - 1];
                    switch(type) {
                        case 1:
                            if (userQuota < quota && quota - userQuota >= dataSize) {
                                this.addStat(id++, submitTime, dataId, dataSize, userId);
                            } else {
                                break taskfor;
                            }
                            break;
                        case 2:
                            if (userQuota < newQuota && newQuota - userQuota >= dataSize) {
                                this.addStat(id++, submitTime, dataId, dataSize, userId);
                            } else {
                                break taskfor;
                            }
                            break;
                        case 3:
                            if (userId == 1) {
                                if (userQuota < newQuota && newQuota - userQuota >= dataSize) {
                                    this.addStat(id++, submitTime, dataId, dataSize, userId);
                                } else {
                                    break taskfor;
                                }
                            } else if (userQuota < diffQuota && diffQuota - userQuota >= dataSize) {
                                this.addStat(id++, submitTime, dataId, dataSize, userId);
                            } else {
                                break taskfor;
                            }
                            break;
                    }
                }
                if (count++ >= 1024) {
                    stat.executeBatch();
                    conn.commit();
                    count = 0;
                }
                quotas[userId - 1] += dataSize;
                dataId++;
                boolean finished = true;
                finishFor: for (int i = 0; i < QuotaScheduler.NUMBER_OF_USERS; i++) {
                    switch(type) {
                        case 1:
                            if (quotas[i] < quota - MIN_DATA_SIZE) {
                                finished = false;
                                break finishFor;
                            }
                            break;
                        case 2:
                            if (quotas[i] < newQuota - MIN_DATA_SIZE) {
                                finished = false;
                                break finishFor;
                            }
                            break;
                        case 3:
                            if (i == 0 && quotas[0] < newQuota - MAX_DATA_SIZE) {
                                finished = false;
                                break finishFor;
                            } else if (quotas[i] < diffQuota - MAX_DATA_SIZE) {
                                finished = false;
                                break finishFor;
                            }
                            break;
                    }
                }
                if (finished) {
                    break;
                }
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        this.close();
    }
