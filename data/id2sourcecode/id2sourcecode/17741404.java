    public void testIoErrorOnWrite() throws Exception {
        String[] outputs = new String[] { "Output.jad", "Output.jar" };
        for (int i = 0; i < outputs.length; i++) {
            File outputFile = new File(getTestDirectory(), outputs[i]);
            FileOutputStream fos = null;
            FileLock lock = null;
            try {
                fos = new FileOutputStream(outputFile);
                lock = fos.getChannel().lock();
                fos.write(0);
                executeBuildExceptionTarget("PackageTaskTest.testIoErrorOnWrite");
                assertTrue(getBuildException().getCause() instanceof IOException);
            } finally {
                lock.release();
                fos.close();
            }
        }
    }
