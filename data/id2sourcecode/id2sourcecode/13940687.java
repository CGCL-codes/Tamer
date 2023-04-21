    public Element execute(Element inData, List<YParameter> inParams, List<YParameter> outParams) throws CodeletExecutionException {
        setInputs(inData, inParams, outParams);
        String cmd = (String) getParameterValue("command");
        StringWriter out = new StringWriter(8192);
        try {
            ProcessBuilder pb = new ProcessBuilder(cmd);
            pb.redirectErrorStream(true);
            handleOptionalParameters(pb, inData);
            Process proc = pb.start();
            InputStream is = proc.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            char[] buffer = new char[8192];
            int count;
            while ((count = isr.read(buffer)) > 0) out.write(buffer, 0, count);
            isr.close();
            setParameterValue("result", out.toString());
            return getOutputData();
        } catch (Exception e) {
            throw new CodeletExecutionException("Exception executing shell process '" + cmd + "': " + e.getMessage());
        }
    }
