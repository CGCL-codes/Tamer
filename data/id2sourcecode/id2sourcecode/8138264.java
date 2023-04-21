    @SuppressWarnings("unchecked")
    public void execute(ArrayList params, UtilBean utils) {
        File returnFile = new File(utils.getOutputFile().getAbsolutePath() + "\\" + (String) params.get(0));
        perm = new java.io.FilePermission(utils.getOutputFile().getAbsolutePath(), "write,read");
        utils.monitor.log("Found: " + returnFile.getAbsolutePath());
        ArrayList returnVal = new ArrayList();
        try {
            fInput = new FileInputStream(returnFile);
            oInput = new ObjectInputStream(fInput);
            utils.monitor.log("Sending information about the results...");
            returnVal.add((FuzzResponseBean) oInput.readObject());
            utils.getOutput().writeObject(new CommunicationBean("response", returnVal));
            utils.monitor.log("Sending " + params.get(0) + "...");
            while ((result = (ResultBean) oInput.readObject()) != null) {
                returnVal = new ArrayList();
                returnVal.add(result);
                try {
                    utils.getOutput().writeObject(new CommunicationBean("response", returnVal));
                } catch (IOException e) {
                    utils.monitor.log("Failed: could not send requested file");
                    e.printStackTrace();
                }
            }
        } catch (EOFException ex) {
            utils.monitor.log(params.get(0) + "Sent");
            try {
                oInput.close();
                fInput.close();
            } catch (IOException e) {
            }
            try {
                returnVal = new ArrayList();
                returnVal.add(false);
                utils.getOutput().writeObject(new CommunicationBean("response", returnVal));
            } catch (IOException e) {
                utils.monitor.log("Failed to alert client of EOF");
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
            e = new Exception("Did not read in file correctly");
        }
    }
