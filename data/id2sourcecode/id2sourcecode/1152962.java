    @Override
    public void onCommand(User sender, UserLevel level, String channel, String command, String[] args) {
        if (level.compareTo(UserLevel.IRC_OP) < 0) {
            return;
        }
        double totalMemory = Double.valueOf((Runtime.getRuntime().totalMemory() / 1048576));
        double usedMemory = totalMemory - Double.valueOf((Runtime.getRuntime().freeMemory() / 1048576));
        double percentUsed = usedMemory / (totalMemory / 100.0);
        double percentUsedCPU = Utilities.getThreadCPUUsage();
        String criticalColorMemory = getCriticalColor(percentUsed);
        String criticalColorCPU = getCriticalColor(percentUsedCPU);
        StringBuilder messageBuilder = new StringBuilder();
        messageBuilder.append("The JVM is able to use up to ");
        messageBuilder.append(Math.round(totalMemory));
        messageBuilder.append(" MB of memory. For the current tasks it uses ");
        messageBuilder.append(Math.round(usedMemory));
        messageBuilder.append(" MB of memory (");
        messageBuilder.append(criticalColorMemory);
        messageBuilder.append(Math.round(percentUsed));
        messageBuilder.append("%");
        messageBuilder.append(Colors.NORMAL);
        messageBuilder.append("). At the moment ");
        messageBuilder.append(criticalColorCPU);
        messageBuilder.append(Math.round(percentUsedCPU));
        messageBuilder.append("%");
        messageBuilder.append(Colors.NORMAL);
        messageBuilder.append(" of all CPU capacities are used for the process.");
        Main.getInstance().getIRCHandler().sendMessage(channel, messageBuilder.toString());
    }
