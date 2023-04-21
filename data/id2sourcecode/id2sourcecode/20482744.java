        public byte[] digest() {
            if (count != 0) {
                if (count % 32 != 0) {
                    int limit = 32 * ((count + 31) / 32);
                    System.arraycopy(ALL_ZEROES, 0, buffer, count, limit - count);
                    count += limit - count;
                }
                byte[] y = nh32(count);
                Y.write(y, 0, 8);
            }
            byte[] A = Y.toByteArray();
            Y.reset();
            byte[] B;
            if (totalCount <= UMac32.L1_KEY_LEN) {
                if (A.length == 0) B = l2hash.digest(); else {
                    B = new byte[16];
                    System.arraycopy(A, 0, B, 8, 8);
                }
            } else {
                if (A.length != 0) l2hash.update(A, 0, A.length);
                B = l2hash.digest();
            }
            byte[] result = l3hash.digest(B);
            reset();
            return result;
        }
