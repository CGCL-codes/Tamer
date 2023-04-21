        private long computeMethodHash() {
            long hash = 0;
            ByteArrayOutputStream sink = new ByteArrayOutputStream(512);
            try {
                MessageDigest md = MessageDigest.getInstance("SHA");
                DataOutputStream out = new DataOutputStream(new DigestOutputStream(sink, md));
                String methodString = nameAndDescriptor();
                out.writeUTF(methodString);
                out.flush();
                byte hashArray[] = md.digest();
                for (int i = 0; i < Math.min(8, hashArray.length); i++) {
                    hash += ((long) (hashArray[i] & 0xFF)) << (i * 8);
                }
            } catch (IOException e) {
                throw new AssertionError(e);
            } catch (NoSuchAlgorithmException e) {
                throw new AssertionError(e);
            }
            return hash;
        }
