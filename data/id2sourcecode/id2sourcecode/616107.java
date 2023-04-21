    public boolean executeAntTarget(String filename, String target, BuildListener listener) {
        boolean ret = true;
        Date dateStart = new Date();
        frame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        File buildFile = new File(filename);
        project = new Project();
        project.setUserProperty("ant.file", buildFile.getAbsolutePath());
        if (logger instanceof DefaultLogger) {
            logger.setErrorPrintStream(System.err);
            logger.setOutputPrintStream(System.out);
            logger.setMessageOutputLevel(message_output_level);
        }
        project.addBuildListener(logger);
        if (listener != null) project.addBuildListener(listener);
        try {
            project.fireBuildStarted();
            project.init();
            ProjectHelper helper = ProjectHelper.getProjectHelper();
            project.addReference("ant.projectHelper", helper);
            helper.parse(project, buildFile);
            project.executeTarget(target);
            project.fireBuildFinished(null);
            Date dateEnd = new Date();
            SimpleDateFormat formatNew = new SimpleDateFormat("s.SSS");
            Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            double duration = (dateEnd.getTime() - dateStart.getTime()) / 1000;
            statistics.addValues(formatter.format(dateEnd), filename, target, duration);
        } catch (Exception e) {
            project.fireBuildFinished(e);
            project = null;
            ret = false;
        }
        frame.setCursor(Cursor.getDefaultCursor());
        return ret;
    }
