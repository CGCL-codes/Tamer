    public static Reader getReader(final WriterRepresentation representation) throws IOException {
        Reader result = null;
        if (Edition.CURRENT != Edition.GAE) {
            final java.io.PipedWriter pipedWriter = new java.io.PipedWriter();
            final java.io.PipedReader pipedReader = new java.io.PipedReader(pipedWriter);
            final org.restlet.Application application = org.restlet.Application.getCurrent();
            org.restlet.service.TaskService taskService = (application == null) ? new org.restlet.service.TaskService() : application.getTaskService();
            taskService.execute(new Runnable() {

                public void run() {
                    try {
                        representation.write(pipedWriter);
                        pipedWriter.close();
                    } catch (IOException ioe) {
                        Context.getCurrentLogger().log(Level.FINE, "Error while writing to the piped reader.", ioe);
                    }
                }
            });
            result = pipedReader;
        } else {
            Context.getCurrentLogger().log(Level.WARNING, "The GAE edition is unable to return a reader for a writer representation.");
        }
        return result;
    }
