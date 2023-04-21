    public void execute(SFSClientListener listener, SFSClient sfsClient, Client client) {
        try {
            ShareFileWriter writer = new ShareFileWriter(part, new File(sfsClient.getShareFolder() + part.getName()));
            InputStream in = client.getSocket().getInputStream();
            LocalShare ls = sfsClient.getLocalShares().get(hash);
            byte[] buf = new byte[client.getSocket().getReceiveBufferSize()];
            int read;
            long tot = 0;
            while (tot < part.getSize()) {
                read = in.read(buf);
                writer.write(buf, read);
                tot += read;
                listener.receiveStatus(ls, part, partNumber, read);
            }
            ls.incShares();
            if (ls.getShares() == ls.getTotalShares()) {
                listener.receiveDone(ls);
                sfsClient.getClient().sendObject(new DownloadCompleteEvent(hash));
            }
        } catch (IOException ex) {
            Logger.getLogger(TransferShareEvent.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (client != null && client.getSocket() != null) {
                    client.getSocket().close();
                }
            } catch (IOException ex) {
                Logger.getLogger(TransferShareEvent.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
