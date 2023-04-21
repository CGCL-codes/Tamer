    public void load() {
        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new FileWriter(outFile));
            out.write("#Thread\tRequest Time\tResponse Time (ms)\n");
            out.flush();
            for (int i = 0; i < iterations; i++) {
                Thread.sleep(getRandomMillisecondsToWait());
                ObtainUserReputation obtainUserReputationRequest = new ObtainUserReputation();
                ObtainUserReputationResponse obtainUserReputationResponse;
                obtainUserReputationRequest.setIoi(null);
                obtainUserReputationRequest.setServiceId(null);
                obtainUserReputationRequest.setUserId(org.gridtrust.trs.v2.test.Constants.USER_PREFIX + org.gridtrust.trs.v2.test.Constants.USER_TEST_SUFFIX);
                obtainUserReputationRequest.setVbeId(org.gridtrust.trs.v2.test.Constants.VBE_ID_1);
                obtainUserReputationRequest.setVoId(org.gridtrust.trs.v2.test.Constants.VO_ID_1);
                Date start = new Date();
                obtainUserReputationResponse = trsPort.obtainUserReputation(obtainUserReputationRequest);
                Date end = new Date();
                out.write(Thread.currentThread().getId() + "\t" + (start.getTime() - timeZero.getTime()) + "\t" + (end.getTime() - start.getTime()) + "\n");
                out.flush();
                Thread.sleep(getRandomMillisecondsToWait());
                RateUser rateUserRequest = new RateUser();
                RateUserResponse rateUserRequestResponse;
                rateUserRequest.setActionId(org.gridtrust.trs.v2.test.Constants.ACTION_GOOD_CONTENT);
                rateUserRequest.setServiceId(org.gridtrust.trs.v2.test.Constants.SERVICE_PREFIX + org.gridtrust.trs.v2.test.Constants.SERVICE_TEST_SUFFIX);
                rateUserRequest.setUserId(org.gridtrust.trs.v2.test.Constants.USER_PREFIX + org.gridtrust.trs.v2.test.Constants.USER_TEST_SUFFIX);
                rateUserRequest.setVbeId(org.gridtrust.trs.v2.test.Constants.VBE_ID_1);
                rateUserRequest.setVoId(org.gridtrust.trs.v2.test.Constants.VO_ID_1);
                start = new Date();
                rateUserRequestResponse = trsPort.rateUser(rateUserRequest);
                end = new Date();
                out.write(Thread.currentThread().getId() + "\t" + (start.getTime() - timeZero.getTime()) + "\t" + (end.getTime() - start.getTime()) + "\n");
                out.flush();
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
