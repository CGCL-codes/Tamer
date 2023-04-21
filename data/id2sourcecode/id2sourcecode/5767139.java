    void writeMarkerDQT(OutputStream os) throws IOException {
        if (!valid) throw new IOException("Can't write marker DQT, because an error happened at reading (" + getLocationName() + ")");
        os.write(M_PRX);
        os.write(M_DQT);
        int size = 2 + q_ix.length * (1 + DCTSIZE2);
        os.write(size >> 8);
        os.write(size & 255);
        for (int i = 0; i < q_ix.length; i++) {
            os.write(q_ix[i] + (q_prec[i] == 8 ? 0 : 0x10));
            for (int k = 0; k < DCTSIZE2; k++) os.write(q_table[i][k]);
        }
    }
