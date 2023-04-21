    private void writeProcessorTriggers(final PrintWriter out, final Set<ObjectName> processors) throws MalformedObjectNameException {
        for (final ObjectName processor : processors) {
            final String name = name(processor);
            final ObjectName threadpool = new ObjectName("jboss.web:type=ThreadPool,name=" + name);
            final String port = port(name);
            final String address = address(name);
            if (name.startsWith("http")) {
                writeTrigger(out, "gzip compression is off for connector " + name + " on {HOSTNAME}", "{{HOSTNAME}:jmx[jboss.web:type=ProtocolHandler,port=" + port + ",address=" + address + "][compression].str(off)}=1", 2);
            }
            writeTrigger(out, "70% " + name + " worker threads busy on {HOSTNAME}", "{{HOSTNAME}:jmx[" + threadpool + "][currentThreadsBusy].last(0)}>({{HOSTNAME}:jmx[" + threadpool + "][maxThreads].last(0)}*0.7)", 4);
        }
    }
