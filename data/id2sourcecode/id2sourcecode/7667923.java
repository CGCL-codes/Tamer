    protected static boolean getParams(String[] args) {
        if (args.length < 3) {
            logger.error("Need the configuration file as first argument then at least one of\n" + "   -wglob limitGlobalWrite\n" + "   -rglob limitGlobalRead\n" + "   -wsess limitSessionWrite\n" + "   -rsess limitSessionWrite");
            return false;
        }
        if (!FileBasedConfiguration.setClientConfigurationFromXml(args[0])) {
            logger.error("Need the configuration file as first argument then at least one of\n" + "   -wglob limitGlobalWrite\n" + "   -rglob limitGlobalRead\n" + "   -wsess limitSessionWrite\n" + "   -rsess limitSessionWrite");
            return false;
        }
        for (int i = 1; i < args.length; i++) {
            if (args[i].equalsIgnoreCase("-wglob")) {
                i++;
                swriteGlobalLimit = Long.parseLong(args[i]);
            } else if (args[i].equalsIgnoreCase("-rglob")) {
                i++;
                sreadGlobalLimit = Long.parseLong(args[i]);
            } else if (args[i].equalsIgnoreCase("-wsess")) {
                i++;
                swriteSessionLimit = Long.parseLong(args[i]);
            } else if (args[i].equalsIgnoreCase("-rsess")) {
                i++;
                sreadSessionLimit = Long.parseLong(args[i]);
            }
        }
        if (swriteGlobalLimit == -1 && sreadGlobalLimit == -1 && swriteSessionLimit == -1 && sreadSessionLimit == -1) {
            logger.error("Need the configuration file as first argument then at least one of\n" + "   -wglob limitGlobalWrite\n" + "   -rglob limitGlobalRead\n" + "   -wsess limitSessionWrite\n" + "   -rsess limitSessionWrite");
            return false;
        }
        return true;
    }
