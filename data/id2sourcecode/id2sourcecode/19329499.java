    private void sendUpdateMessage(long ms) {
        try {
            ManagedObject temp = AppContext.getDataManager().getBindingForUpdate(Server.userInputData);
            if (temp instanceof ScalableList<?>) {
                ScalableList<?> s = (ScalableList<?>) temp;
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                DataOutputStream dos = new DataOutputStream(baos);
                dos.writeLong(ms);
                Iterator<?> i = s.iterator();
                while (i.hasNext()) {
                    dos.write(((DataPacket) i.next()).getData());
                }
                ByteBuffer buffer = ByteBuffer.wrap(baos.toByteArray());
                Channel c = AppContext.getChannelManager().getChannel(Server.allChannelName);
                c.send(null, buffer);
            }
        } catch (IOException a) {
        }
    }
