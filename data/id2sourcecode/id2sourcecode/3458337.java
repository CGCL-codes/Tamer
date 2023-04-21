    private void writeProcessorItems(final PrintWriter out, final Set<ObjectName> processors) throws MalformedObjectNameException {
        for (final ObjectName processor : processors) {
            final String name = name(processor);
            final ObjectName threadpool = new ObjectName("Catalina:type=ThreadPool,name=" + name);
            final String port = name.substring(name.indexOf('-') + 1);
            writeItem(out, name + " bytes received per second", processor, "bytesReceived", Type.Float, "B", Store.AsDelta, Time.TwicePerMinute);
            writeItem(out, name + " bytes sent per second", processor, "bytesSent", Type.Float, "B", Store.AsDelta, Time.TwicePerMinute);
            writeItem(out, name + " requests per second", processor, "requestCount", Type.Float, null, Store.AsDelta, Time.TwicePerMinute);
            writeItem(out, name + " errors per second", processor, "errorCount", Type.Float, null, Store.AsDelta, Time.TwicePerMinute);
            writeItem(out, name + " processing time per second", processor, "processingTime", Type.Float, "s", Store.AsDelta, Time.TwicePerMinute);
            writeItem(out, name + " threads max", threadpool, "maxThreads", Type.Integer, null, Store.AsIs, Time.OncePerHour);
            writeItem(out, name + " threads allocated", threadpool, "currentThreadCount", Type.Integer, null, Store.AsIs, Time.TwicePerMinute);
            writeItem(out, name + " threads busy", threadpool, "currentThreadsBusy", Type.Integer, null, Store.AsIs, Time.TwicePerMinute);
            if (name.startsWith("http")) {
                writeItem(out, name + " gzip compression", new ObjectName("Catalina:type=ProtocolHandler,port=" + port), "compression", Type.Character, null, Store.AsIs, Time.OncePerHour);
            }
        }
    }
