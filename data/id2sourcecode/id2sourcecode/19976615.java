    public PJointChannel getChannel(String jointName) {
        for (PJointChannel channel : m_JointChannels) if (channel.getTargetJointName().equals(jointName)) return channel;
        return null;
    }
