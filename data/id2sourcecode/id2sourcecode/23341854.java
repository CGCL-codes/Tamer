    public void store(final KerberosTicket ticket, final EncryptionKey key) throws KrbCredCacheException {
        File file;
        RandomAccessFile raf;
        FileLock lock;
        try {
            file = new File(this.file);
            if (!file.isFile() || !file.exists()) {
                throw new KrbCredCacheException("file does not exist");
            }
            raf = new RandomAccessFile(this.file, "rwd");
            lock = raf.getChannel().lock();
            raf.seek(raf.length());
        } catch (Exception e) {
            throw new KrbCredCacheException("cannot open credentials file: " + this.file, e);
        }
        try {
            storePrincipal(raf, ticket.getClient());
            storePrincipal(raf, ticket.getServer());
            storeShort(raf, (short) key.getEType());
            storeShort(raf, (short) key.getEType());
            storeData(raf, key.getBytes());
            storeInt(raf, (int) (ticket.getAuthTime().getTime() / 1000));
            storeInt(raf, (int) (ticket.getStartTime().getTime() / 1000));
            storeInt(raf, (int) (ticket.getEndTime().getTime() / 1000));
            if (ticket.getRenewTill() != null) {
                storeInt(raf, (int) (ticket.getRenewTill().getTime() / 1000));
            } else {
                storeInt(raf, 0);
            }
            storeByte(raf, 0);
            storeFlags(raf, ticket.getFlags());
            storeAddresses(raf, ticket.getClientAddresses());
            storeInt(raf, 1);
            storeShort(raf, (short) ticket.getSessionKeyType());
            storeData(raf, ticket.getSessionKey().getEncoded());
            storeData(raf, ticket.getEncoded());
            storeInt(raf, 0);
        } catch (KrbCredCacheException ke) {
            throw new KrbCredCacheException("unable to write ticket", ke);
        }
        try {
            lock.release();
            raf.getChannel().close();
        } catch (Exception e) {
            throw new KrbCredCacheException("cannot close credentials file: " + this.file, e);
        }
        return;
    }
