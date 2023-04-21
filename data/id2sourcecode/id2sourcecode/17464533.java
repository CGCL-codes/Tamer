    public Packet handleVirtualMachine$AllThreads(Packet cmd) {
        cmd = PacketFactory.changeToReply(cmd);
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(buffer);
        try {
            out.write(ByteUtil.writeInteger(getThreadCount()));
            for (RuntimeThread thread : allRuntimeThreads()) {
                out.write(ByteUtil.writeInteger(thread.getIdentifier()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        cmd.data = buffer.toByteArray();
        return cmd;
    }
