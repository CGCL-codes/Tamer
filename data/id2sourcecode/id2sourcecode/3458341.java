    private void writeProcessorTriggers(final PrintWriter out, final Set<ObjectName> processors) throws MalformedObjectNameException {
        for (final ObjectName processor : processors) {
            final String name = name(processor);
            final ObjectName threadpool = new ObjectName("Catalina:type=ThreadPool,name=" + name);
            final String port = name.substring(name.indexOf('-') + 1);
            if (name.startsWith("http")) {
                writeTrigger(out, "gzip compression is off for connector " + name + " on {HOSTNAME}", "{{HOSTNAME}:jmx[Catalina:type=ProtocolHandler,port=" + port + "][compression].str(off)}=1", 2);
            }
            writeTrigger(out, "70% " + name + " worker threads busy on {HOSTNAME}", "{{HOSTNAME}:jmx[" + threadpool + "][currentThreadsBusy].last(0)}>({{HOSTNAME}:jmx[" + threadpool + "][maxThreads].last(0)}*0.7)", 4);
        }
    }
