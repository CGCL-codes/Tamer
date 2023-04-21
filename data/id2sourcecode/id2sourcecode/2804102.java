    @Override
    public void send(boolean sign, int[] targets, TOMMessage sm) {
        if (sm.serializedMessage == null) {
            DataOutputStream dos = null;
            try {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                dos = new DataOutputStream(baos);
                sm.wExternal(dos);
                dos.flush();
                sm.serializedMessage = baos.toByteArray();
            } catch (IOException ex) {
                Logger.println("Impossible to serialize message: " + sm);
            } finally {
                try {
                    dos.close();
                } catch (IOException ex) {
                }
            }
        }
        if (sign && sm.serializedMessageSignature == null) {
            sm.serializedMessageSignature = signMessage(manager.getStaticConf().getRSAPrivateKey(), sm.serializedMessage);
        }
        int sent = 0;
        for (int i = targets.length - 1; i >= 0; i--) {
            sm.destination = targets[i];
            rl.readLock().lock();
            Channel channel = ((NettyClientServerSession) sessionTable.get(targets[i])).getChannel();
            rl.readLock().unlock();
            if (channel.isConnected()) {
                sm.signed = sign;
                channel.write(sm);
                sent++;
            } else {
                Logger.println("Channel to " + targets[i] + " is not connected");
            }
        }
        if (sent < manager.getCurrentViewF() + 1) {
            throw new RuntimeException("Impossible to connect to servers!");
        }
    }
