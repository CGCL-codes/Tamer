    public StatusOutputType waitForCompletion() throws JobManagerException {
        logger.info("called");
        if (!started) {
            String msg = "Can't wait for a remote job that hasn't be started";
            logger.error(msg);
            throw new JobManagerException(msg);
        }
        int code = -1;
        try {
            StatusOutputType status = appServicePort.queryStatus(remoteJobID);
            code = appServicePort.queryStatus(remoteJobID).getCode();
            while (code != GramJob.STATUS_DONE && code != GramJob.STATUS_FAILED) {
                status = appServicePort.queryStatus(remoteJobID);
                code = status.getCode();
                status.setCode(code);
                Thread.sleep(3000);
            }
        } catch (RemoteException e) {
            logger.error("RemoteException in WaitForActivation - " + e.getMessage());
        } catch (Exception e) {
            logger.error("Exception in WaitForActivation - " + e.getMessage());
        }
        try {
            JobOutputType out = appServicePort.getOutputs(remoteJobID);
            OutputFileType[] outfile = out.getOutputFile();
            String[] relPath;
            String outPath;
            if (outfile != null) {
                relPath = new String[outfile.length];
                for (int j = 0; j < outfile.length; j++) {
                    String u = outfile[j].getUrl().toString();
                    int index = u.indexOf('/' + remoteJobID + '/');
                    relPath[j] = u.substring(index + remoteJobID.length() + 2);
                    BufferedInputStream in = new BufferedInputStream(new java.net.URL(outfile[j].getUrl().toString()).openStream());
                    if (relPath[j].indexOf("/") != -1) {
                        index = relPath[j].lastIndexOf("/");
                        String d = workdir + File.separator + relPath[j].substring(0, index);
                        boolean b = new File(d).mkdirs();
                        outPath = workdir + File.separator + relPath[j];
                    } else outPath = workdir + File.separator + outfile[j].getName();
                    FileOutputStream fos = new FileOutputStream(outPath);
                    java.io.BufferedOutputStream bout = new BufferedOutputStream(fos, 1024);
                    byte[] data = new byte[1024];
                    int x = 0;
                    while ((x = in.read(data, 0, 1024)) >= 0) bout.write(data, 0, x);
                    bout.close();
                    in.close();
                }
            }
            String[] stdfiles = { "stdout.txt", "stderr.txt" };
            try {
                for (int i = 0; i < stdfiles.length; i++) {
                    BufferedInputStream in = new BufferedInputStream(new java.net.URL(remoteBaseURL + "/" + stdfiles[i]).openStream());
                    FileOutputStream fos = new FileOutputStream(workdir + File.separator + stdfiles[i]);
                    java.io.BufferedOutputStream bout = new BufferedOutputStream(fos, 1024);
                    byte[] data = new byte[1024];
                    int x = 0;
                    while ((x = in.read(data, 0, 1024)) >= 0) bout.write(data, 0, x);
                    bout.close();
                    in.close();
                }
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        } catch (RemoteException e) {
            logger.error("RemoteException in WaitForCompletion - " + e.getMessage());
        } catch (Exception e) {
            logger.error("Exception in WaitForCompletion - " + e.getMessage());
        }
        String smsg;
        if (code == GramJob.STATUS_DONE) {
            smsg = "Successfully completed on remote: <A HREF=" + remoteBaseURL + ">" + remoteBaseURL + "</A>";
            logger.info(smsg);
            status.setCode(GramJob.STATUS_DONE);
            status.setMessage(smsg);
        } else {
            smsg = "Failed on remote: <A HREF=" + remoteBaseURL + ">" + remoteBaseURL + "</A>";
            logger.info(smsg);
            status.setCode(GramJob.STATUS_FAILED);
            status.setMessage(smsg);
        }
        return status;
    }
