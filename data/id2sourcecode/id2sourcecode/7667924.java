    public static void main(String[] args) {
        InternalLoggerFactory.setDefaultFactory(new GgSlf4JLoggerFactory(null));
        if (logger == null) {
            logger = GgInternalLoggerFactory.getLogger(ChangeBandwidthLimits.class);
        }
        if (!getParams(args)) {
            logger.error("Wrong initialization");
            if (DbConstant.admin != null && DbConstant.admin.isConnected) {
                DbConstant.admin.close();
            }
            System.exit(1);
        }
        long time1 = System.currentTimeMillis();
        R66Future future = new R66Future(true);
        Configuration.configuration.pipelineInit();
        NetworkTransaction networkTransaction = new NetworkTransaction();
        try {
            ChangeBandwidthLimits transaction = new ChangeBandwidthLimits(future, swriteGlobalLimit, sreadGlobalLimit, swriteSessionLimit, sreadSessionLimit, networkTransaction);
            transaction.run();
            future.awaitUninterruptibly();
            long time2 = System.currentTimeMillis();
            long delay = time2 - time1;
            R66Result result = future.getResult();
            if (future.isSuccess()) {
                if (result.code == ErrorCode.Warning) {
                    logger.warn("WARNED on bandwidth:\n    " + (result.other != null ? ((ValidPacket) result.other).getSheader() : "no file") + "\n    delay: " + delay);
                } else {
                    logger.warn("SUCCESS on Bandwidth:\n    " + (result.other != null ? ((ValidPacket) result.other).getSheader() : "no file") + "\n    delay: " + delay);
                }
            } else {
                if (result.code == ErrorCode.Warning) {
                    logger.warn("Bandwidth is\n    WARNED", future.getCause());
                    networkTransaction.closeAll();
                    System.exit(result.code.ordinal());
                } else {
                    logger.error("Bandwidth in\n    FAILURE", future.getCause());
                    networkTransaction.closeAll();
                    System.exit(result.code.ordinal());
                }
            }
        } finally {
            networkTransaction.closeAll();
        }
    }
