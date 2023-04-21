    public boolean startup() throws ModuleException {
        try {
            alive = false;
            NagiosPluginLoader loader = NagiosPluginLoader.getInstance();
            loader.load();
            resourceList = new ResourceInternalManager();
            NagiosPluginCore pluginCore = NagiosPluginCore.getInstance();
            pluginCore.loadDef();
            NagiosLineParser lineParser = NagiosLineParser.getInstance();
            lineParser.setUp();
            BrainyConfiguration configuration = BrainyConfiguration.getInstance();
            String aFileName = createDataNagiosDir(configuration);
            String bufferWriterName = aFileName + File.separatorChar + configuration.getString("nagios.buffer.writer.logname");
            WriterThread writer = createBufferWriter(bufferWriterName, configuration);
            resourceList.addAndStartResource(writer);
            String bufferDiscardName = aFileName + File.separatorChar + configuration.getString("nagios.discard.writer.logname");
            discard = createDiscardWriter(bufferDiscardName, configuration);
            resourceList.addAndStartResource(discard);
            String noPerfdataFoundName = aFileName + File.separatorChar + configuration.getString("nagios.discard.noperfdatafound.logname");
            noPerfDataFound = createDiscardWriter(noPerfdataFoundName, configuration);
            resourceList.addAndStartResource(noPerfDataFound);
            ObjectFIFO<NagiosLineInfo> bufferDispatcher = new ObjectFIFO<NagiosLineInfo>();
            dispatcher = new Dispatcher(writer, bufferDispatcher);
            resourceList.addAndStartResource(dispatcher);
            String alertNagiosLog = configuration.getString("nagios.alert.writer.dirname") + File.separatorChar + configuration.getString("nagios.alert.writer.logname");
            alertLog = createAlertWriter(alertNagiosLog, configuration);
            resourceList.addAndStartResource(alertLog);
            int numberOfParserThread = configuration.getInt("nagios.plugin.parserThread.number");
            String threadParserName = configuration.getString("nagios.plugin.parserThread.name");
            long sleepTime = configuration.getLong("nagios.plugin.parserThread.sleepTime");
            for (int i = 0; i < numberOfParserThread; i++) {
                ThreadParser threadParser = new ThreadParser(threadParserName + "-" + i, discard, noPerfDataFound, sleepTime, bufferDispatcher, alertLog);
                resourceList.addAndStartResource(threadParser);
            }
            int numberOfListener = configuration.getInt("nagios.plugin.listenerThread.number");
            String threadListenerName = configuration.getString("nagios.plugin.listenerThread.name");
            long sleepThreadTime = configuration.getLong("nagios.plugin.listenerThread.sleepTime");
            String charset = configuration.getString("nagios.listener.charset");
            ObjectFIFO<Socket> queue = new ObjectFIFO<Socket>();
            for (int i = 0; i < numberOfListener; i++) {
                String name = threadListenerName + "-" + i;
                logger.debug("Starting listener: " + name);
                ListenerThread listenerThread = new ListenerThread(name, charset, dispatcher, sleepThreadTime, queue);
                resourceList.addAndStartResource(listenerThread);
            }
            ListenerSrv srv = new ListenerSrv(queue);
            resourceList.addAndStartResource(srv);
            alive = true;
            return alive;
        } catch (Throwable ex) {
            logger.fatal("Module not started:", ex);
            return false;
        }
    }
