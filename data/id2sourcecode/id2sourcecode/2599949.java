    public void run() {
        String sJob = "";
        JDCConnection oConsumerConnection;
        if (DebugFile.trace) {
            DebugFile.writeln("Begin WorkerThread.run()");
            DebugFile.incIdent();
            DebugFile.writeln("thread=" + getName());
        }
        bContinue = true;
        sLastError = "";
        while (bContinue) {
            try {
                sleep(delay);
                if (DebugFile.trace) DebugFile.writeln(getName() + " getting next atom...");
                oAtm = oConsumer.next();
                if (oAtm == null) {
                    if (DebugFile.trace) DebugFile.writeln(getName() + " no more atoms.");
                    if (iCallbacks > 0) callBack(WorkerThreadCallback.WT_ATOMCONSUMER_NOMORE, "Thread " + getName() + " no more Atoms", null, oConsumer);
                    break;
                }
                if (iCallbacks > 0) callBack(WorkerThreadCallback.WT_ATOM_GET, "Thread " + getName() + " got Atom " + String.valueOf(oAtm.getInt(DB.pg_atom)), null, oAtm);
                oConsumerConnection = oConsumer.getConnection();
                if (DebugFile.trace) DebugFile.writeln(getName() + " AtomConsumer.getConnection() : " + (oConsumerConnection != null ? "[Conenction]" : "null"));
                if (!sJob.equals(oAtm.getString(DB.gu_job))) {
                    sJob = oAtm.getString(DB.gu_job);
                    try {
                        oJob = Job.instantiate(oConsumerConnection, sJob, oPool.getProperties());
                        if (iCallbacks > 0) callBack(WorkerThreadCallback.WT_JOB_INSTANTIATE, "instantiate job " + sJob + " command " + oJob.getString(DB.id_command), null, oJob);
                    } catch (ClassNotFoundException e) {
                        sJob = "";
                        oJob = null;
                        sLastError = "Job.instantiate(" + sJob + ") ClassNotFoundException " + e.getMessage();
                        if (DebugFile.trace) DebugFile.writeln(getName() + " " + sLastError);
                        if (iCallbacks > 0) callBack(-1, sLastError, e, null);
                        bContinue = false;
                    } catch (IllegalAccessException e) {
                        sJob = "";
                        oJob = null;
                        sLastError = "Job.instantiate(" + sJob + ") IllegalAccessException " + e.getMessage();
                        if (DebugFile.trace) DebugFile.writeln(getName() + " " + sLastError);
                        if (iCallbacks > 0) callBack(-1, sLastError, e, null);
                        bContinue = false;
                    } catch (InstantiationException e) {
                        sJob = "";
                        oJob = null;
                        sLastError = "Job.instantiate(" + sJob + ") InstantiationException " + e.getMessage();
                        if (DebugFile.trace) DebugFile.writeln(getName() + " " + sLastError);
                        if (iCallbacks > 0) callBack(-1, sLastError, e, null);
                        bContinue = false;
                    } catch (SQLException e) {
                        sJob = "";
                        oJob = null;
                        sLastError = " Job.instantiate(" + sJob + ") SQLException " + e.getMessage();
                        if (DebugFile.trace) DebugFile.writeln(getName() + " " + sLastError);
                        if (iCallbacks > 0) callBack(-1, sLastError, e, null);
                        bContinue = false;
                    }
                }
                if (null != oJob) {
                    oJob.process(oAtm);
                    if (DebugFile.trace) DebugFile.writeln("Thread " + getName() + " consumed Atom " + String.valueOf(oAtm.getInt(DB.pg_atom)));
                    oAtm.archive(oConsumerConnection);
                    if (iCallbacks > 0) callBack(WorkerThreadCallback.WT_ATOM_CONSUME, "Thread " + getName() + " consumed Atom " + String.valueOf(oAtm.getInt(DB.pg_atom)), null, oAtm);
                    oAtm = null;
                    if (DebugFile.trace) DebugFile.writeln("job " + oJob.getString(DB.gu_job) + " pending " + String.valueOf(oJob.pending()));
                    if (oJob.pending() == 0) {
                        oJob.setStatus(oConsumerConnection, Job.STATUS_FINISHED);
                        if (iCallbacks > 0) callBack(WorkerThreadCallback.WT_JOB_FINISH, "finish", null, oJob);
                    }
                } else {
                    oAtm = null;
                    sLastError = "Job.instantiate(" + sJob + ") returned null";
                    if (DebugFile.trace) DebugFile.writeln("ERROR: " + sLastError);
                    if (iCallbacks > 0) callBack(-1, sLastError, new NullPointerException("Job.instantiate(" + sJob + ")"), null);
                    bContinue = false;
                }
                oConsumerConnection = null;
            } catch (FileNotFoundException e) {
                if (DebugFile.trace) DebugFile.writeln(getName() + " FileNotFoundException " + e.getMessage());
                if (null != oJob) {
                    sLastError = "FileNotFoundException, job " + oJob.getString(DB.gu_job) + " ";
                    if (null != oAtm) sLastError += "atom " + String.valueOf(oAtm.getInt(DB.pg_atom)) + " ";
                    sLastError += e.getMessage();
                    oJob.log(getName() + " FileNotFoundException, job " + oJob.getString(DB.gu_job) + " ");
                    if (null != oAtm) oJob.log("atom " + String.valueOf(oAtm.getInt(DB.pg_atom)) + " ");
                    oJob.log(e.getMessage() + "\n");
                } else sLastError = "FileNotFoundException " + e.getMessage();
                if (iCallbacks > 0) callBack(-1, sLastError, e, oJob);
                bContinue = false;
            } catch (IOException e) {
                if (DebugFile.trace) DebugFile.writeln(getName() + " IOException " + e.getMessage());
                if (null != oJob) {
                    sLastError = "IOException, job " + oJob.getString(DB.gu_job) + " " + e.getMessage();
                    if (null != oAtm) sLastError += "atom " + String.valueOf(oAtm.getInt(DB.pg_atom)) + " ";
                    sLastError += e.getMessage();
                    oJob.log(getName() + " IOException, job " + oJob.getString(DB.gu_job) + " ");
                    if (null != oAtm) oJob.log("atom " + String.valueOf(oAtm.getInt(DB.pg_atom)) + " ");
                    oJob.log(e.getMessage() + "\n");
                } else sLastError = "IOException " + e.getMessage();
                if (iCallbacks > 0) callBack(-1, sLastError, e, oJob);
                bContinue = false;
            } catch (SQLException e) {
                if (DebugFile.trace) DebugFile.writeln(getName() + " SQLException " + e.getMessage());
                if (null != oJob) {
                    sLastError = "SQLException, job " + oJob.getString(DB.gu_job) + " ";
                    if (null != oAtm) sLastError += "atom " + String.valueOf(oAtm.getInt(DB.pg_atom)) + " ";
                    sLastError += e.getMessage();
                    oJob.log(getName() + " SQLException, job " + oJob.getString(DB.gu_job) + " ");
                    if (null != oAtm) oJob.log("atom " + String.valueOf(oAtm.getInt(DB.pg_atom)) + " ");
                    oJob.log(e.getMessage() + "\n");
                } else sLastError = "SQLException " + e.getMessage();
                if (iCallbacks > 0) callBack(-1, sLastError, e, oJob);
                bContinue = false;
            } catch (MessagingException e) {
                if (DebugFile.trace) DebugFile.writeln(getName() + " MessagingException " + e.getMessage());
                if (null != oJob) {
                    sLastError = "MessagingException, job " + oJob.getString(DB.gu_job) + " ";
                    if (null != oAtm) sLastError += "atom " + String.valueOf(oAtm.getInt(DB.pg_atom)) + " ";
                    sLastError += e.getMessage();
                    oJob.log(getName() + " MessagingException, job " + oJob.getString(DB.gu_job) + " ");
                    if (null != oAtm) oJob.log("atom " + String.valueOf(oAtm.getInt(DB.pg_atom)) + " ");
                    oJob.log(e.getMessage() + "\n");
                } else sLastError = "MessagingException " + e.getMessage();
                if (iCallbacks > 0) callBack(-1, sLastError, e, oJob);
                bContinue = false;
            } catch (InterruptedException e) {
                if (DebugFile.trace) DebugFile.writeln(getName() + " InterruptedException " + e.getMessage());
                if (null != oJob) {
                    sLastError = "InterruptedException, job " + oJob.getString(DB.gu_job) + " ";
                    if (null != oAtm) sLastError += "atom " + String.valueOf(oAtm.getInt(DB.pg_atom)) + " ";
                    sLastError += e.getMessage();
                    oJob.log(getName() + " InterruptedException, job " + oJob.getString(DB.gu_job) + " ");
                    if (null != oAtm) oJob.log("atom " + String.valueOf(oAtm.getInt(DB.pg_atom)) + " ");
                    oJob.log(e.getMessage() + "\n");
                } else sLastError = "InterruptedException " + e.getMessage();
                if (iCallbacks > 0) callBack(-1, sLastError, e, oJob);
                bContinue = false;
            } finally {
                sJob = "";
                oJob = null;
                oAtm = null;
            }
        }
        if (DebugFile.trace) {
            DebugFile.decIdent();
            DebugFile.writeln("End WorkerThread.run()");
        }
    }
