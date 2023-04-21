        @Override
        public TaskState call() throws Exception {
            String path = serverConfig.getTaskUrlMap().get(state.getTaskName());
            if (path == null) {
                throw new RuntimeException("ServerConfig task Url mapping missing for task " + state.getTaskName());
            }
            String url = serverConfig.getBaseUrl() + path;
            HttpUriRequest req = task.createTaskRequest(state, url);
            try {
                final long start = System.currentTimeMillis();
                HttpResponse res = httpClient.execute(req, new BasicHttpContext());
                if (log.isTraceEnabled()) {
                    log.trace(state.getDate() + " " + state.getNodeState().getNodeId() + " " + state.getTaskName() + " HTTP response: " + res.getStatusLine());
                }
                EntityUtils.consume(res.getEntity());
                long reqTime = System.currentTimeMillis() - start;
                long successCount = state.getSuccessCount().incrementAndGet();
                state.getSuccessTotalMs().addAndGet(reqTime);
                state.adjustDate(Calendar.MINUTE, loadConfig.getFrequency());
                if (log.isInfoEnabled() && successCount % 5 == 0) {
                    log.info(state.getNodeState().getNodeId() + " " + state.getTaskName() + " completed " + successCount + " tasks");
                }
            } catch (Exception e) {
                req.abort();
                if (log.isDebugEnabled()) {
                    log.debug("Failed HTTP request: " + e.getMessage());
                }
                throw e;
            }
            if (!state.isFinished()) {
                state.getNodeState().getExecutor().submit(new NodeTaskCallable(httpClient, state, task, serverConfig, loadConfig));
            } else {
                if (log.isDebugEnabled()) {
                    log.debug(state.getNodeState().getNodeId() + " " + state.getTaskName() + " finished");
                }
            }
            return state;
        }
