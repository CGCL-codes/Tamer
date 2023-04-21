    private void signHelper(int workerId, int requestId, Map<Integer, byte[]> dataGroups, boolean signerDoesHashing, String digestAlg, String sigAlg) throws Exception {
        Map<Integer, byte[]> expectedHashes;
        if (signerDoesHashing) {
            MessageDigest d = MessageDigest.getInstance(digestAlg, "BC");
            expectedHashes = new HashMap<Integer, byte[]>();
            for (Map.Entry<Integer, byte[]> entry : dataGroups.entrySet()) {
                expectedHashes.put(entry.getKey(), d.digest(entry.getValue()));
                d.reset();
            }
        } else {
            expectedHashes = dataGroups;
        }
        SODSignResponse res = (SODSignResponse) sSSession.process(workerId, new SODSignRequest(requestId, dataGroups), new RequestContext());
        assertNotNull(res);
        assertEquals(requestId, res.getRequestID());
        Certificate signercert = res.getSignerCertificate();
        assertNotNull(signercert);
        byte[] sodBytes = res.getProcessedData();
        SODFile sod = new SODFile(new ByteArrayInputStream(sodBytes));
        boolean verify = sod.checkDocSignature(signercert);
        assertTrue("Signature verification", verify);
        Map<Integer, byte[]> actualDataGroupHashes = sod.getDataGroupHashes();
        assertEquals(expectedHashes.size(), actualDataGroupHashes.size());
        for (Map.Entry<Integer, byte[]> entry : actualDataGroupHashes.entrySet()) {
            assertTrue("DG" + entry.getKey(), Arrays.equals(expectedHashes.get(entry.getKey()), entry.getValue()));
        }
        assertEquals(digestAlg, sod.getDigestAlgorithm());
        assertEquals(sigAlg, sod.getDigestEncryptionAlgorithm());
    }
