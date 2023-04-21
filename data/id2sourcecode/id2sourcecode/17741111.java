    @Override
    protected void submit(Job pJob) throws Exception {
        try {
            String path = Base.getI().getJobDirectory(pJob.getId());
            List<DataStagingType> val = InputHandler.getInputs(pJob);
            Vector valc = new Vector();
            String param = new String(""), str = "";
            for (int i = 0; i < val.size(); i++) {
                FileInputStream in = new FileInputStream(path + val.get(i).getFileName());
                byte[] btmp = new byte[512];
                int itmp = 0;
                String paramName = "";
                boolean first = true;
                while ((itmp = in.read(btmp)) > (-1)) {
                    if (first) param = param.concat(paramName + "=" + (new String(btmp, 0, itmp))); else param = param.concat("&" + paramName + "=" + (new String(btmp, 0, itmp)));
                    first = false;
                }
            }
            URL url = new URL(pJob.getConfiguredResource().getVo());
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
            writer.write(param);
            writer.flush();
            writer.close();
            byte[] buf = new byte[1024];
            File f = new File(Base.getI().getJobDirectory(pJob.getId()) + "outputs/" + OutputHandler.getOutputs(pJob.getJSDL()).get(0).getName());
            f.createNewFile();
            FileOutputStream fw = new FileOutputStream(f);
            InputStream is = conn.getInputStream();
            int bs = 10;
            while ((bs = is.read(buf)) > 0) {
                fw.write(buf, 0, bs);
                fw.flush();
            }
            is.close();
            fw.flush();
            fw.close();
            pJob.setStatus(ActivityStateEnumeration.FINISHED);
        } catch (Exception e) {
            File f = new File(Base.getI().getJobDirectory(pJob.getId()) + "outputs/stderr.log");
            f.createNewFile();
            FileWriter fw = new FileWriter(f);
            fw.write("Middleware:" + pJob.getConfiguredResource().getMiddleware());
            fw.write("VO:" + pJob.getConfiguredResource().getVo());
            fw.write("Resource:" + pJob.getConfiguredResource().getResource());
            fw.write("Jobmanager:" + pJob.getConfiguredResource().getJobmanager());
            fw.write(e.getMessage() + "\n");
            for (int ii = 0; ii < e.getStackTrace().length; ii++) fw.write(e.getStackTrace()[ii].toString() + "\n");
            fw.flush();
            fw.close();
            pJob.setStatus(ActivityStateEnumeration.FAILED);
        }
    }
