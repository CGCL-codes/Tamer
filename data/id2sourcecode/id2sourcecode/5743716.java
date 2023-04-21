    public void httpVsHttps() {
        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new FileWriter(outFile));
            out.write("#Thread\tRequest Time\tResponse Time (ms)\n");
            out.flush();
            Date start = null;
            Date end = null;
            for (int i = 0; i < iterations; i++) {
                Thread.sleep(getConstantMillisecondsToWait());
                ObtainUserReputation obtainUserReputationRequest = new ObtainUserReputation();
                ObtainUserReputationResponse obtainUserReputationResponse;
                obtainUserReputationRequest.setIoi(null);
                obtainUserReputationRequest.setServiceId(null);
                obtainUserReputationRequest.setUserId("u0001");
                obtainUserReputationRequest.setVbeId("1");
                obtainUserReputationRequest.setVoId("vo1");
                start = new Date();
                obtainUserReputationResponse = trsPort.obtainUserReputation(obtainUserReputationRequest);
                end = new Date();
                out.write(Thread.currentThread().getId() + "\t" + cnt + "\t" + (end.getTime() - start.getTime()) + "\n");
                out.flush();
                cnt++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.flush();
                    out.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
