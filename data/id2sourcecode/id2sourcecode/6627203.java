    protected byte[] fingerprint(BigInteger[] quantities) {
        synchronized (shactx) {
            for (int i = 0; i < quantities.length; i++) {
                byte[] mpi = Util.MPIbytes(quantities[i]);
                shactx.update(mpi, 0, mpi.length);
            }
            return shactx.digest();
        }
    }
