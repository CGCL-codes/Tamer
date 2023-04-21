    synchronized SignedPair getPair(byte[] key) {
        MessageDigest digest = Crypt.getDigest();
        byte[] curHash = new byte[digest.getDigestLength()];
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
                if (isDebugPair) System.out.println(iface.getAddress() + ".getPair(" + address + ")");
                try {
                    if (iface.routesToMe(address)) {
                        return (SignedPair) iface.realGet(key);
                    }
                    int curoff = proto.makeGetPairPacket(address, buffer, localTask, key);
                    boolean success = proto.outputAndWait(pairTask, buffer, 0, curoff, address);
                    if (!success) {
                        if (isDebugPair) System.out.println(iface.getAddress() + ".getPair(" + ") failed @" + address);
                    } else {
                        if (pairTask.replyLen == 0) throw new RuntimeException("replyBuffer is empty");
                        int[] offp = new int[1];
                        offp[0] += proto.getLinkHeaderLen() + 2;
                        SignedPair pair = SignedPair.deserialize(pairTask.replyBuffer, offp);
                        if (offp[0] > pairTask.replyLen) {
                            System.out.println(iface.getAddress() + ".get " + pairTask.replyBuffer + "," + Tests.bufferToString(pairTask.replyBuffer, pairTask.replyLen, offp[0]) + " length exceeded " + offp[0] + " > " + pairTask.replyLen);
                            throw new RuntimeException("Malformed packet - length exceeded");
                        }
                        if (isDebugPair) System.out.println(iface.getAddress() + ".get " + pairTask.replyBuffer + "," + Tests.bufferToString(pairTask.replyBuffer, pairTask.replyLen, offp[0]));
                        pairTask.replyLen = 0;
                        if (pair == null) continue;
                        return pair;
                    }
                } catch (InterruptedException ie) {
                    System.err.println("ChordProto: getPair timeout @" + address);
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                    System.err.println("ChordProto: getPair IO exception @" + address);
                }
            }
        } catch (DigestException e) {
            e.printStackTrace();
            throw new RuntimeException(e.toString());
        } finally {
            iface.deleteTask(localTask);
        }
        return null;
    }
