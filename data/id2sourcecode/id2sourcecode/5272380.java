    public void generate(int numOfJobs, double reuse) throws Exception {
        this.createDatabase();
        int submitTime = 0;
        int threshold = numOfJobs - (int) (numOfJobs * (reuse / 100));
        int reuseLimit = numOfJobs - threshold;
        System.out.println("-- LIMIAR: " + threshold);
        System.out.println("-- REUSE LIMIT: " + reuseLimit);
        int[] datas = new int[threshold];
        for (int i = 0; i < threshold; i++) {
            random = new Random(System.nanoTime());
            datas[i] = MIN_DATA_SIZE + random.nextInt(MAX_DATA_SIZE - MIN_DATA_SIZE);
        }
        int id = 1;
        int count = 0;
        int dataId = 1;
        for (int i = 0; i < threshold; i++) {
            try {
                random = new Random(System.nanoTime());
                int userId = 1 + random.nextInt(QuotaScheduler.NUMBER_OF_USERS);
                this.addStat(id++, submitTime, dataId, datas[dataId - 1], userId);
                if (count++ == 1024) {
                    stat.executeBatch();
                    conn.commit();
                    count = 0;
                }
                submitTime += SUBMISSION_DELAY;
                dataId++;
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }
        }
        for (int i = 0; i < reuseLimit; ) {
            random = new Random(System.nanoTime());
            dataId = 1 + random.nextInt(threshold);
            int randomSubmitTime = random.nextInt(submitTime);
            int tasks = MIN_TASKS + random.nextInt(MAX_TASKS - MIN_TASKS);
            int userId = 1 + random.nextInt(QuotaScheduler.NUMBER_OF_USERS);
            for (int j = 0; j < tasks && i < reuseLimit; j++, i++) {
                this.addStat(id++, randomSubmitTime, dataId, datas[dataId - 1], userId);
                if (count++ == 1024) {
                    stat.executeBatch();
                    conn.commit();
                    count = 0;
                }
            }
        }
        this.close();
    }
