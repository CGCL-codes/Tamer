    protected byte[] exec(String command, byte[] convertable, Session session, Map<String, Object> properties) throws ConverterException {
        ExecutorService threadPool = Executors.newFixedThreadPool(3);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayOutputStream errOut = new ByteArrayOutputStream();
        ChannelExec channel = null;
        try {
            channel = (ChannelExec) session.openChannel("exec");
            channel.setCommand(command);
            if (properties != null) {
                for (String key : properties.keySet()) {
                    logger.debug("setting environment property: " + key + "=" + properties.get(key));
                    channel.setEnv(key, (String) properties.get(key));
                }
            }
            channel.connect();
            InputStream in = channel.getInputStream();
            ReadStreamThread readInput = new ReadStreamThread(in, out);
            threadPool.execute(readInput);
            InputStream errIn = channel.getErrStream();
            ReadStreamThread readError = new ReadStreamThread(errIn, errOut);
            threadPool.execute(readError);
            OutputStream channelOut = channel.getOutputStream();
            ReadStreamThread writeRequest = new ReadStreamThread(new ByteArrayInputStream(convertable), channelOut);
            threadPool.execute(writeRequest);
            threadPool.shutdown();
            while (true) {
                if (threadPool.isTerminated()) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Closing conversion thread. Finished.");
                    }
                    break;
                }
                if (logger.isDebugEnabled()) {
                    logger.debug("Waiting for ssh response to return...");
                }
                Thread.sleep(500);
            }
            String error = errOut.toString("UTF-8");
            if (error != null && error.length() > 0) {
                throw new ConverterException(error);
            }
        } catch (JSchException e) {
            throw new ConverterException(e);
        } catch (IOException e) {
            throw new ConverterException(e);
        } catch (InterruptedException e) {
            throw new ConverterException(e);
        } finally {
            if (channel != null) {
                channel.disconnect();
            }
        }
        return out.toByteArray();
    }
