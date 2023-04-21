    private void log(String str) {
        _logger.setMsg(str);
        Thread writer = new Thread(_logger);
        writer.setName("logger");
        writer.setPriority(Thread.MIN_PRIORITY);
        writer.start();
    }
