    public boolean open() throws DBException {
        try {
            if (!opened) {
                FileOutputStream fos = new FileOutputStream(file);
                fc = fos.getChannel();
                lock = fc.tryLock();
                if (lock == null) {
                    System.err.println("FATAL ERROR: Cannot open '" + file.getName() + "' for exclusive access");
                    System.exit(1);
                }
                BufferedOutputStream bos = new BufferedOutputStream(fos, BUFFER_SIZE);
                dos = new DataOutputStream(bos);
                opened = true;
            }
            return opened;
        } catch (Exception e) {
            throw new DBException(FaultCodes.GEN_CRITICAL_ERROR, "Error opening " + file.getName(), e);
        }
    }
