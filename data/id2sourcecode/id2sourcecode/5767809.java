    @Override
    public void send(int[] targets, TOMMessage sm, boolean serializeClassHeaders) {
        DataOutputStream dos = null;
        byte[] data = null;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            dos = new DataOutputStream(baos);
            sm.wExternal(dos);
            dos.flush();
            data = baos.toByteArray();
            sm.serializedMessage = data;
        } catch (IOException ex) {
            Logger.println("Error enconding message.");
        } finally {
            try {
                dos.close();
            } catch (IOException ex) {
                Logger.println("Exception closing DataOutputStream: " + ex.getMessage());
            }
        }
        sm.signed = false;
        if (sm.signed) {
            byte[] data2 = TOMUtil.signMessage(manager.getStaticConf().getRSAPrivateKey(), data);
            sm.serializedMessageSignature = data2;
        }
        for (int i = 0; i < targets.length; i++) {
            rl.readLock().lock();
            NettyClientServerSession ncss = (NettyClientServerSession) sessionTable.get(targets[i]);
            if (ncss != null) {
                Channel session = ncss.getChannel();
                rl.readLock().unlock();
                sm.destination = targets[i];
                sendLock.lock();
                session.write(sm);
                sendLock.unlock();
            } else {
                rl.readLock().unlock();
            }
        }
    }
