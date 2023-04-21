    public C_Emblem(byte abyte0[], ClientThread clientthread) throws Exception {
        super(abyte0);
        L1PcInstance player = clientthread.getActiveChar();
        if (player.getClanid() != 0) {
            String emblem_file = String.valueOf(player.getClanid());
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream("emblem/" + emblem_file);
                for (short cnt = 0; cnt < 384; cnt++) {
                    fos.write(readC());
                }
            } catch (Exception e) {
                _log.log(Level.SEVERE, e.getLocalizedMessage(), e);
                throw e;
            } finally {
                if (null != fos) {
                    fos.close();
                }
                fos = null;
            }
            player.sendPackets(new S_Emblem(player.getClanid()));
            L1World.getInstance().broadcastPacketToAll(new S_Emblem(player.getClanid()));
        }
    }
