        void write(byte[] data) {
            byte[] lsb_msb = new byte[2];
            lsb_msb[0] = (byte) data.length;
            lsb_msb[1] = (byte) ((data.length >> 8) & 0xff);
            try {
                os.write(concat(lsb_msb, data));
                os.flush();
            } catch (IOException e) {
                Log.e(TAG, "WriteThread write error ", e);
            }
        }
