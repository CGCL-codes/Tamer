    synchronized void putPair(SignedPair pair) {
        byte[] key = pair.getKey();
        MessageDigest digest = Crypt.getDigest();
        byte[] curHash = new byte[digest.getDigestLength()];
        pairTask.replyLen = 0;
        int localTask = iface.newTask(pairTask);
        try {
            for (int replica = 0; replica < pairReplicas; replica++) {
                if (replica == 0) {
                    digest.update(key, 0, key.length);
                } else {
                    digest.update(curHash, 0, curHash.length);
                }
                digest.digest(curHash, 0, curHash.length);
                ChordAddress address = new ChordAddress(curHash, 0);
                if (isDebugPair) System.out.println(iface.getAddress() + ".putPair(" + address + ")");
                try {
                    if (iface.routesToMe(address)) {
                        iface.realPut(pair);
                        if (isDebugPair) System.out.println(iface.getAddress() + ".putPair(" + ")=myself @" + address);
                    } else {
                        int curoff = proto.makePutPairPacket(address, buffer, localTask, pair);
                        boolean success = proto.outputAndWait(pairTask, buffer, 0, curoff, address);
                        if (!success) {
                            if (isDebugPair) System.out.println(iface.getAddress() + ".putPair(" + ") failed @" + address);
                        }
                        pairTask.replyLen = 0;
                    }
                } catch (InterruptedException ie) {
                    System.err.println("ChordProto: putPair timeout @" + address);
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                    System.err.println("ChordProto: putPair IO exception @" + address);
                }
            }
        } catch (DigestException e) {
            e.printStackTrace();
            throw new RuntimeException(e.toString());
        } finally {
            iface.deleteTask(localTask);
        }
        return;
    }
