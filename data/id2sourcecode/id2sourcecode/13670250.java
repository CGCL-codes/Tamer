    public void buildRunnerEnv() throws Exception {
        report.report("Build runner env");
        kill();
        clean();
        if (jarListFileExist) {
            Properties p = FileUtils.loadPropertiesFromFile(runnerOutJarListProperties.getAbsolutePath());
            String requiredJars = p.getProperty("thirdpartyLib");
            REQUIRED_JARS = createJarArray(requiredJars);
            String jsystemJars = p.getProperty("lib");
            JSYSTEM_JARS = createJarArray(jsystemJars);
            String jsystemThirdparty = p.getProperty("thirdpartyCommonLib");
            JSYSTEM_THIRDPARTY = createJarArray(jsystemThirdparty);
            File outLib = new File(distRunOut, "lib");
            File thirdparty = new File(distRunOut, "thirdparty");
            File thirdpartyLib = new File(thirdparty, "lib");
            File thirdpartyCommonLib = new File(thirdparty, "commonLib");
            File ant = new File(thirdparty, "ant");
            if (runnerSourceDir == null) {
                runnerSourceDir = System.getProperty("user.dir");
            }
            if (regressionSourceDir == null) {
                regressionSourceDir = regressionPath;
            }
            report.report("copying newJregression.jar from " + regressionSourceDir + File.separatorChar + "lib" + File.separator + "newJregression.jar" + " to ");
            report.report(distRunOut + File.separator + "lib" + File.separator + "newJregression.jar");
            FileUtils.copyFile(new File(regressionSourceDir + File.separatorChar + "lib" + File.separator + "newJregression.jar"), new File(distRunOut + File.separator + "lib" + File.separator + "newJregression.jar"));
            for (int i = 0; i < REQUIRED_JARS.length; i++) {
                FileUtils.copyFile(new File(runnerSourceDir + File.separator + "thirdparty" + File.separator + "lib" + File.separator + REQUIRED_JARS[i]), new File(thirdpartyLib, REQUIRED_JARS[i]));
            }
            for (int i = 0; i < JSYSTEM_THIRDPARTY.length; i++) {
                FileUtils.copyFile(new File(runnerSourceDir + File.separator + "thirdparty" + File.separator + "commonLib" + File.separator + JSYSTEM_THIRDPARTY[i]), new File(thirdpartyCommonLib, JSYSTEM_THIRDPARTY[i]));
            }
            for (int i = 0; i < JSYSTEM_JARS.length; i++) {
                FileUtils.copyFile(new File(runnerSourceDir + File.separator + "lib" + File.separator + JSYSTEM_JARS[i]), new File(outLib, JSYSTEM_JARS[i]));
            }
            FileUtils.copyDirectory(new File(runnerOutDir + File.separator + "thirdparty" + File.separator + "ant"), ant);
        } else if (!jarListFileExist) {
            FileUtils.copyDirectory(runnerSourceDir + File.separator + "lib", distRunOut.getAbsolutePath() + File.separatorChar + "lib");
            FileUtils.copyDirectory(runnerSourceDir + File.separator + "thirdparty", distRunOut.getAbsolutePath() + File.separatorChar + "thirdparty");
            FileUtils.copyFile(regressionPath + File.separatorChar + "lib" + File.separatorChar + "newJregression.jar", distRunOut.getAbsolutePath() + File.separatorChar + "lib" + File.separatorChar + "newJregression.jar");
        }
        if (jsystemPropertiesFile.exists()) {
            jsystemPropertiesFile.delete();
        }
        setInitialJsystemProperties();
        if (dbProperties.exists()) {
            dbProperties.delete();
        }
        FileUtils.copyFile(new File(runnerSourceDir + File.separator + DBProperties.DB_PROPERTIES_FILE), new File(envDir.getAbsolutePath() + File.separator + "runner" + File.separator + DBProperties.DB_PROPERTIES_FILE));
        FileUtils.copyFile(new File(runnerSourceDir + File.separator + "run.bat"), new File(envDir.getAbsolutePath() + File.separator + "runner" + File.separator + "run.bat"));
        FileUtils.copyFile(new File(runnerSourceDir + File.separator + "run"), new File(envDir.getAbsolutePath() + File.separator + "runner" + File.separator + "run"));
        FileUtils.copyFile(new File(runnerSourceDir + File.separator + "runBase.bat"), new File(envDir.getAbsolutePath() + File.separator + "runner" + File.separator + "runBase.bat"));
        FileUtils.copyFile(new File(runnerSourceDir + File.separator + "runBase"), new File(envDir.getAbsolutePath() + File.separator + "runner" + File.separator + "runBase"));
        FileUtils.copyFile(new File(runnerSourceDir + File.separator + "runAgent.bat"), new File(envDir.getAbsolutePath() + File.separator + "runner" + File.separator + "runAgent.bat"));
        FileUtils.copyFile(new File(runnerSourceDir + File.separator + "runAgent"), new File(envDir.getAbsolutePath() + File.separator + "runner" + File.separator + "runAgent"));
    }
