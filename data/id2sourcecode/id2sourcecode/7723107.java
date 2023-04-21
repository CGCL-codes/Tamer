    public void testExecuteTask() throws IOException {
        ClassLoader cls = Thread.currentThread().getContextClassLoader();
        InputStream is = cls.getResourceAsStream("META-INF/spring/openfrwk-module-tasks.xml");
        if (is == null) {
            this.logger.error("Not found file");
            throw new IllegalStateException("Could not perform test without the given file");
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int read = 0;
        while ((read = is.read()) != -1) {
            baos.write(read);
        }
        HashMap context = new HashMap();
        context.put(MyJobTask.XML_BYTES, baos.toByteArray());
        context.put(MyJobTask.FILE_NAME, "META-INF/spring/openfrwk-module-tasks.xml");
        TaskExecutor.executeTask(MyJobTask.class, context);
        logger.info("Executed Task " + MyJobTask.class.getName());
        try {
            Thread.sleep(4 * 1000);
        } catch (InterruptedException e) {
        }
    }
