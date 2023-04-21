    public void testRunInLoop() throws Exception {
        jsystem.setJSystemProperty(FrameworkOptions.RUN_MODE, "" + getRunMode());
        jsystem.launch();
        report.step("execute test that get the current test folder");
        ScenarioUtils.createAndCleanScenario(jsystem, "RunInLoop");
        jsystem.addTest("testReportFor10Sec", "GenericBasic", 3);
        report.report("Scenario was created");
        for (int i = 0; i < getRepeatAmount(); i++) {
            report.report("in iteration number " + i);
            jsystem.initReporters();
            report.report("Give the init reporter some time to clean logs ... ");
            Thread.sleep(10000);
            jsystem.play();
            Thread.sleep(3000);
            jsystem.playPause();
            Thread.sleep(2000);
            jsystem.play();
            jsystem.waitForRunEnd();
            report.report("give the reporter thread some time to write log.");
            Thread.sleep(3000);
            report.step("check that the test end successful and the report step was added");
            jsystem.checkNumberOfTestsPass(3);
        }
    }
