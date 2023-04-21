        public void data(InputStream data) throws RejectException, TooMuchDataException, IOException {
            List<Deliverer> deliveries = this.makeDeliverers();
            if (deliveries.size() == 1) {
                deliveries.get(0).deliver(data);
            } else {
                DeferredFileOutputStream dfos = new DeferredFileOutputStream(DATA_DEFERRED_SIZE);
                try {
                    int value;
                    while ((value = data.read()) >= 0) dfos.write(value);
                    boolean anyoneAtAll = false;
                    Exception lastProblem = null;
                    for (Deliverer deliv : deliveries) {
                        try {
                            deliv.deliver(dfos.getInputStream());
                            anyoneAtAll = true;
                        } catch (Exception ex) {
                            log.error("Error delivering to " + deliv.toString(), ex);
                            lastProblem = ex;
                        }
                    }
                    if (!anyoneAtAll) {
                        if (lastProblem instanceof IOException) throw (IOException) lastProblem; else if (lastProblem instanceof RejectException) throw (RejectException) lastProblem; else if (lastProblem instanceof RuntimeException) throw (RuntimeException) lastProblem; else throw new RuntimeException(lastProblem);
                    }
                } finally {
                    dfos.close();
                }
            }
        }
