    @Test
    public void testDigest() throws Exception {
        System.out.println("==== Digests");
        Operation<byte[], String> digest = new Operation<byte[], String>() {

            Object execute(byte[] input, String algorithm) throws Exception {
                MessageDigest md = MessageDigest.getInstance(algorithm);
                md.update(input);
                return md.digest();
            }
        };
        for (int i = 0; i < digestAlgorithms.length; ++i) {
            System.out.println("--- Raw = digest only of byte[] using " + digestAlgorithms[i]);
            for (int j = 0; j < payloads.length; ++j) {
                runBenchmark("raw digest (" + payloads[j].length + " bytes)", digest, payloads[j], digestAlgorithms[i]);
            }
            System.out.println("");
        }
        Operation<ContentObject, String> digestObj = new Operation<ContentObject, String>() {

            Object execute(ContentObject input, String algorithm) throws Exception {
                MessageDigest md = MessageDigest.getInstance(algorithm);
                DigestOutputStream dos = new DigestOutputStream(new NullOutputStream(), md);
                input.encode(dos);
                return md.digest();
            }
        };
        for (int i = 0; i < digestAlgorithms.length; ++i) {
            System.out.println("--- Raw = digest of contentObject using " + digestAlgorithms[i]);
            for (int j = 0; j < contentObjects.length; ++j) {
                runBenchmark("ContentObject digest (content " + contentObjects[j].contentLength() + " bytes) ", digestObj, contentObjects[j], digestAlgorithms[i]);
            }
            System.out.println("");
        }
    }
