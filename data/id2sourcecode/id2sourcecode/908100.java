    public void destroy() throws Exception {
        String[] rmcom = { "sh", "-c", "rm output/" + id_seq + "/*" };
        Process rm = Runtime.getRuntime().exec(rmcom);
        rm.waitFor();
        rm.getErrorStream().close();
        rm.getInputStream().close();
        rm.getOutputStream().close();
        rm.destroy();
    }
