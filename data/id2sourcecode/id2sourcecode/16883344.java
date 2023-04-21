    public TestResult call() {
        Logger logger = Logger.getLogger("testing");
        logger.log(Level.INFO, "Thread about to run test with ID " + test.getId() + " for submission with ID " + submission.getId());
        IDAOSession daoSession = null;
        try {
            try {
                DAOFactory f = (DAOFactory) FactoryRegistrar.getFactory(DAOFactory.class);
                daoSession = f.getInstance();
            } catch (FactoryException e) {
                throw new AdminUtilityException("dao init error", e);
            }
            daoSession.beginTransaction();
            Map<String, String> parameters = daoSession.getTestDAOInstance().getTestParameters(test.getId());
            ITestMethod testMethod = (ITestMethod) Class.forName(test.getTestClassName()).newInstance();
            ITestExecutor testExecutor = (ITestExecutor) Class.forName(test.getExecutorClassName()).newInstance();
            IResourceDAO resourceDao = daoSession.getResourceDAOInstance();
            Resource testResource = resourceDao.retrievePersistentEntity(test.getLibraryResourceId());
            InputStream submissionInputStream = resourceDao.openInputStream(submission.getResourceId());
            InputStream testInputStream = resourceDao.openInputStream(test.getLibraryResourceId());
            File testResourceDirectory = new File(testTempDirectory.getAbsolutePath() + File.separator + "test_resource");
            File submissionDirectory = new File(testTempDirectory.getAbsoluteFile() + File.separator + "submission");
            if (!testResourceDirectory.mkdir()) {
                throw new IOException("Could not create " + testResourceDirectory);
            }
            if (!submissionDirectory.mkdir()) {
                throw new IOException("Could not create " + submissionDirectory);
            }
            extractResource(submissionInputStream, submissionDirectory);
            if (testResource.getMimeType().equals("application/zip")) {
                logger.log(Level.DEBUG, "Extracting test resource as it is a zip file...");
                try {
                    extractResource(testInputStream, testResourceDirectory);
                } catch (IOException e) {
                    logger.log(Level.ERROR, "Could not extract test resource: not a zip file?");
                    logger.log(Level.ERROR, e);
                }
            } else {
                logger.log(Level.DEBUG, "Copying test resource as it is not a zip file...");
                File testResourceFile = new File(testResourceDirectory.getAbsolutePath() + File.separator + "resource");
                FileOutputStream fos = new FileOutputStream(testResourceFile);
                byte buffer[] = new byte[4096];
                int nread = -1;
                while ((nread = testInputStream.read(buffer)) != -1) {
                    fos.write(buffer, 0, nread);
                }
                fos.flush();
                fos.close();
            }
            submissionInputStream.close();
            testInputStream.close();
            File submissionFilesDirectory = new File(submissionDirectory.getAbsolutePath() + File.separator + submission.getResourceSubdirectory());
            for (File file : submissionFilesDirectory.listFiles()) {
                if (!file.renameTo(new File(submissionDirectory.getAbsolutePath() + File.separator + file.getName()))) {
                    throw new IOException("could not move " + file + " to submission directory");
                }
            }
            submissionFilesDirectory.delete();
            daoSession.endTransaction();
            ExecutionResult executionResult = testExecutor.execute(testResourceDirectory, submissionDirectory, test.getCommand(), test.getMaximumExecutionTime());
            TestResult result = testMethod.test(parameters, executionResult);
            if (result.getOutput() == null) {
                result.setOutput("Test had no output");
            }
            if (result.getComment() == null) {
                result.setComment("Test had no comment");
            }
            if (!killDirectory(testTempDirectory, true)) {
                logger.log(Level.WARN, "didn't delete test directory - " + testTempDirectory);
            }
            return result;
        } catch (Exception e) {
            if (daoSession != null) {
                daoSession.abortTransaction();
            }
            if (testTempDirectory != null) {
                if (!killDirectory(testTempDirectory, true)) {
                    logger.log(Level.WARN, "didn't delete test directory - " + testTempDirectory);
                }
            }
            TestResult output = new TestResult();
            StringWriter stringWriter = new StringWriter();
            e.printStackTrace(new PrintWriter(stringWriter));
            output.setComment("Un-recoverable error");
            output.setOutput(stringWriter.getBuffer().toString());
            output.setFinishTime(new Date());
            output.setResult(0);
            output.setMaxMark(0);
            return output;
        }
    }
