    private void loadTests(String strConfigXmlPath) throws Exception {
        InputStreamReader isr;
        try {
            isr = new FileReader(strConfigXmlPath);
        } catch (FileNotFoundException e) {
            URL url = this.getClass().getClassLoader().getResource(strConfigXmlPath);
            if (null != url) {
                try {
                    isr = new InputStreamReader(url.openStream());
                } catch (Exception e1) {
                    throw new Exception("Unable to find TestCaseConfiguration.xml");
                }
            } else {
                throw new Exception("Could not load test case configuration.");
            }
        }
        TestSuite testsuite = TestSuite.unmarshal(isr);
        isr.close();
        initConnection(testsuite.getTestConnection());
        TestCase testCase[] = testsuite.getTestCase();
        TestMethod testMethod[];
        TestScenario testScenario[];
        TestType testType[];
        ArrayList arrlTestCases;
        TestCaseConfig TestCaseConfig;
        testMap = new HashMap();
        for (int tc = 0; tc < testCase.length; tc++) {
            arrlTestCases = new ArrayList();
            testMethod = testCase[tc].getTestMethod();
            for (int tm = 0; tm < testMethod.length; tm++) {
                testScenario = testMethod[tm].getTestScenario();
                for (int ts = 0; ts < testScenario.length; ts++) {
                    if (testScenario[ts].getTestTypeCount() > 0) {
                        testType = testScenario[ts].getTestType();
                    } else if (testMethod[tm].getTestTypeCount() > 0) {
                        testType = testMethod[tm].getTestType();
                    } else {
                        TestCaseConfig = createTestConfig(testCase[tc], testMethod[tm], testScenario[ts]);
                        arrlTestCases.add(TestCaseConfig);
                        continue;
                    }
                    for (int tt = 0; tt < testType.length; tt++) {
                        TestCaseConfig = createTestConfig(testCase[tc], testMethod[tm], testScenario[ts]);
                        TestCaseConfig.setTestType(testType[tt].getTesttypeid());
                        if (null != testType[tt].getPerfparams()) {
                            TestCaseConfig.setPerfParams(getPerfParams(testType[tt]));
                        }
                        arrlTestCases.add(TestCaseConfig);
                    }
                }
            }
            testMap.put(testCase[tc].getName(), arrlTestCases);
        }
        if (null != testsuite.getPreTest() && null != testsuite.getPreTest().getTestData()) {
            pretest = new TestCaseConfig();
            createTest(pretest, PRETEST, testsuite.getPreTest().getTestData());
        }
        if (null != testsuite.getPostTest() && null != testsuite.getPostTest().getTestData()) {
            posttest = new TestCaseConfig();
            createTest(posttest, POSTTEST, testsuite.getPostTest().getTestData());
        }
    }
