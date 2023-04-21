    public static void chmodUserToRepo(String repoName) throws ApacheFSAccessDenyException {
        Runtime myRun = Runtime.getRuntime();
        try {
            if (System.getProperty("os.name").contains("Win")) {
                Process process = myRun.exec("");
                process.getInputStream().close();
                process.getOutputStream().close();
                process.getErrorStream().close();
            } else if (System.getProperty("os.name").contains("nux")) {
                Process process = myRun.exec("chown " + SVNPropertyLoader.getApacheUser() + ":" + SVNPropertyLoader.getApacheUser() + repoName);
                process.getInputStream().close();
                process.getOutputStream().close();
                process.getErrorStream().close();
            }
        } catch (IOException e) {
            String message = e.getClass().getCanonicalName() + " " + e.getLocalizedMessage();
            for (StackTraceElement element : e.getStackTrace()) {
                message += element.toString();
            }
            GlobalProperties.getMyLogger().severe(message);
            throw new ApacheFSAccessDenyException(e.getMessage());
        }
    }
