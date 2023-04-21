    private void writeProcessorGraphs(final PrintWriter out, final Set<ObjectName> processors) throws MalformedObjectNameException {
        for (final ObjectName processor : processors) {
            final String name = name(processor);
            final ObjectName threadpool = new ObjectName("Catalina:type=ThreadPool,name=" + name);
            writeGraph(out, name + " worker threads", threadpool, "maxThreads", "currentThreadsBusy", "currentThreadCount");
        }
    }
