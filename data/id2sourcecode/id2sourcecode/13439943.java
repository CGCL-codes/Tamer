    @Override
    public void run() {
        Base.writeLogg(THIS_MIDDLEWARE, new LB("starting thread - EDGI"));
        Job tmp = null;
        while (true) {
            try {
                tmp = jobs.take();
                switch(tmp.getFlag()) {
                    case SUBMIT:
                        Base.initLogg(tmp.getId(), "logg.job.submit");
                        submit(tmp);
                        Base.endJobLogg(tmp, LB.INFO, "");
                        tmp.setFlag(GETSTATUS);
                        tmp.setTimestamp(System.currentTimeMillis());
                        tmp.setPubStatus(ActivityStateEnumeration.RUNNING);
                        break;
                    case GETSTATUS:
                        if ((System.currentTimeMillis() - tmp.getTimestamp()) < LAST_ACTIVATE_TIMESTAMP) {
                            try {
                                sleep(1000);
                            } catch (InterruptedException ei) {
                                Base.writeLogg(THIS_MIDDLEWARE, new LB(ei));
                            }
                        } else {
                            Base.initLogg(tmp.getId(), "logg.job.getstatus");
                            getStatus(tmp);
                            Base.endJobLogg(tmp, LB.INFO, "");
                            tmp.setTimestamp(System.currentTimeMillis());
                        }
                        break;
                    case ABORT:
                        tmp.setStatus(ActivityStateEnumeration.CANCELLED);
                        Base.initLogg(tmp.getId(), "logg.job.abort");
                        abort(tmp);
                        Base.endJobLogg(tmp, LB.INFO, "");
                        break;
                }
                if (isEndStatus(tmp)) {
                    Base.initLogg(tmp.getId(), "logg.job.getoutput");
                    getOutputs(tmp);
                    Base.endJobLogg(tmp, LB.INFO, "");
                    Base.getI().finishJob(tmp);
                } else if (isAbortStatus(tmp)) Base.getI().finishJob(tmp); else addJob(tmp);
            } catch (Exception e) {
                if (tmp != null) Base.writeJobLogg(tmp, e, "error.job." + tmp.getFlag());
            }
        }
    }
