    public void testRun() {
        String ip = "10.0.0.1";
        Integer port = 5000;
        fillStorage();
        KnowContacts contacts = getFilledContacts();
        try {
            for (int i = 0; i < values.length; i++) {
                BigInteger randomHash = new BigInteger(digester.digest(new byte[] { (byte) System.currentTimeMillis() }));
                FindValueRPC findValueRPC = new FindValueRPC();
                byte[] hashed = digester.digest(values[i][0].getBytes());
                findValueRPC.setKey(new BigInteger(hashed));
                findValueRPC.setSenderNodeID(new BigInteger(digester.digest(new byte[] { 1 })));
                findValueRPC.setRPCID(randomHash);
                RPCInfo<FindValueRPC> rpcInfo = new RPCInfo<FindValueRPC>(findValueRPC, ip, port);
                FindValueResponseHandler handler = new FindValueResponseHandler();
                handler.setRPCInfo(rpcInfo);
                handler.run();
                RPCInfo removed = buffer.remove();
                assertEquals(ip, removed.getIP());
                assertEquals((int) port, removed.getPort());
                assertEquals(FindValueResponse.class, removed.getRPC().getClass());
                FindValueResponse response = (FindValueResponse) removed.getRPC();
                assertEquals(randomHash, response.getRPCID());
                assertEquals(new BigInteger(values[i][1].getBytes()), response.getValue());
                assertEquals(values[i][1], new String(response.getValue().toByteArray()));
            }
            int findKey = 100;
            BigInteger randomHash = new BigInteger(digester.digest(new byte[] { (byte) System.currentTimeMillis() }));
            FindValueRPC findValueRPC = new FindValueRPC();
            findValueRPC.setKey(new BigInteger(normalizeArray(BigInteger.valueOf(findKey).toByteArray(), 20)));
            findValueRPC.setSenderNodeID(new BigInteger(digester.digest(new byte[] { 1 })));
            findValueRPC.setRPCID(randomHash);
            RPCInfo<FindValueRPC> rpcInfo = new RPCInfo<FindValueRPC>(findValueRPC, ip, port);
            FindValueResponseHandler handler = new FindValueResponseHandler();
            handler.setRPCInfo(rpcInfo);
            handler.setContacts(contacts);
            handler.run();
            assertEquals(20, buffer.size());
            List<FindNodeResponse> responses = new ArrayList<FindNodeResponse>();
            while (!buffer.isEmpty()) {
                RPCInfo<FindNodeResponse> removed = buffer.remove();
                assertEquals(ip, removed.getIP());
                assertEquals((int) port, removed.getPort());
                assertEquals(FindNodeResponse.class, removed.getRPC().getClass());
                FindNodeResponse response = (FindNodeResponse) removed.getRPC();
                responses.add(response);
            }
            Collections.sort(responses, new FindNodeResponseComparator());
            Iterator<FindNodeResponse> it = responses.iterator();
            for (int i = findKey - 90; i <= findKey + 100; i += 10) assertEquals(BigInteger.valueOf(i), it.next().getFoundNodeID());
        } catch (Exception e) {
            fail();
        }
    }
