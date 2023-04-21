            Object execute(ContentObject input, String algorithm) throws Exception {
                MessageDigest md = MessageDigest.getInstance(algorithm);
                DigestOutputStream dos = new DigestOutputStream(new NullOutputStream(), md);
                input.encode(dos);
                return md.digest();
            }
