    protected void schedule() throws Exception {
        try {
            float budget = 0, budgetSpent = 0, budgetLeft = 0;
            long deadline = 0, timeSpent = 0, timeLeft = 0;
            while (!finished) {
                Qos qos = store.getQos(applicationID);
                budget = qos.getBudget();
                budgetSpent = qos.getBudgetSpent();
                budgetLeft = budget - budgetSpent;
                deadline = qos.getDeadline();
                timeSpent = qos.getTimeSpent();
                timeLeft = deadline - timeSpent;
                logger.info("The optimisation is " + ScheduleOptimisationType.stringValue(optimisation));
                logger.info("schedule(): Budget = " + budget + ", Deadline = " + deadline);
                Job jNext = getNextReadyJob();
                if (jNext != null) {
                    List servers = this.getCandidateServers(jNext);
                    switch(optimisation) {
                        case ScheduleOptimisationType.COST_TIME:
                            sortByCost(servers);
                            ArrayList grpSrvLst = new ArrayList();
                            groupServers(grpSrvLst, servers);
                            for (int l = 0; l < grpSrvLst.size(); l++) {
                                ArrayList servList = (ArrayList) grpSrvLst.get(l);
                                for (int p = 0; p < servList.size(); p++) {
                                    ComputeServer server = (ComputeServer) servList.get(p);
                                    float currentAvailBudget = budgetLeft - budgetAllocated;
                                    float budgetAvailPerJob = currentAvailBudget / totalStats.getReadyJobs();
                                    if (budgetAvailPerJob < server.getPricePerJob()) continue;
                                    while (jNext != null) {
                                        budgetAllocated += server.getPricePerJob();
                                        saveJobMapping(jNext, server);
                                        jNext = getNextReadyJob();
                                    }
                                    if (jNext == null) break;
                                }
                                if (jNext == null) break;
                            }
                            break;
                        case ScheduleOptimisationType.COST:
                            sortByCost(servers);
                            if (servers != null) {
                                for (int i = 0; i < servers.size(); i++) {
                                    ComputeServer server = (ComputeServer) servers.get(i);
                                    synchronized (oBudgetAlloc) {
                                        float currentAvailBudget = budgetLeft - budgetAllocated;
                                        float budgetAvailPerJob = currentAvailBudget / totalStats.getReadyJobs();
                                        if (budgetAvailPerJob < server.getPricePerJob()) {
                                            continue;
                                        }
                                    }
                                    long jobsToSubmit = 0;
                                    if (server.getAvgJobComputationTime() != 0) {
                                        jobsToSubmit = Math.round(timeLeft / server.getAvgJobComputationTime());
                                        jobsToSubmit = (jobsToSubmit >= server.getJobLimit()) ? server.getJobLimit() : jobsToSubmit;
                                    } else {
                                        jobsToSubmit = 1;
                                    }
                                    while (jNext != null && jobsToSubmit > 0) {
                                        checkQos(qos);
                                        boolean submitSuccess = saveJobMapping(jNext, server);
                                        if (submitSuccess) {
                                            jNext = getNextReadyJob();
                                            synchronized (oBudgetAlloc) {
                                                budgetAllocated += server.getPricePerJob();
                                            }
                                            jobsToSubmit--;
                                        } else {
                                            break;
                                        }
                                    }
                                    if (jNext == null) break;
                                }
                            }
                            break;
                        case ScheduleOptimisationType.TIME:
                            sortByTime(servers);
                            if (servers != null) {
                                for (int i = 0; i < servers.size(); i++) {
                                    ComputeServer server = (ComputeServer) servers.get(i);
                                    long jobsToSubmit = 0;
                                    if (server.getPricePerJob() != 0) {
                                        jobsToSubmit = Math.round(budgetLeft / server.getPricePerJob());
                                        jobsToSubmit = (jobsToSubmit >= server.getJobLimit()) ? server.getJobLimit() : jobsToSubmit;
                                    } else {
                                        jobsToSubmit = totalStats.getReadyJobs();
                                    }
                                    while (jNext != null && jobsToSubmit > 0) {
                                        checkQos(qos);
                                        boolean submitSuccess = saveJobMapping(jNext, server);
                                        if (submitSuccess) {
                                            jNext = getNextReadyJob();
                                            synchronized (oBudgetAlloc) {
                                                budgetAllocated += server.getPricePerJob();
                                            }
                                            jobsToSubmit--;
                                        } else {
                                            break;
                                        }
                                    }
                                    if (jNext == null) break;
                                }
                            }
                            break;
                    }
                }
                updateStats();
                if (budgetLeft <= 0) {
                    logger.warn("No more budget left. Stopping scheduler.");
                    setFeasible(false);
                    break;
                }
                if (timeLeft <= 0) {
                    logger.warn("No more time left. Stopping scheduler.");
                    setFeasible(false);
                    break;
                }
                numPoll++;
                SleepUtil.sleep(pollTime);
            }
            logger.info("schedule() - Total budget spent " + budgetSpent);
            logger.info("schedule() - Total time spent: " + timeSpent);
        } catch (InterruptedException e) {
            logger.error("Scheduler thread interrupted...", e);
            this.setFailed();
        } catch (Exception e) {
            logger.error("Scheduler thread failed...", e);
            this.setFailed();
        }
    }
