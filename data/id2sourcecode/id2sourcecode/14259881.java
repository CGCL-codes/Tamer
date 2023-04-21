    private List<InetSocketAddressInfo> getPeerList(Hash fileId, Peer peer, int numNeighbors) {
        Map<InetSocketAddressInfo, Peer> peerMap = fileIdToPeerMap.get(fileId);
        int numSharers = peerMap.size();
        int missingNeighbors = ((int) Math.ceil(Math.log(numSharers))) + peerListAnswerConst - numNeighbors;
        List<InetSocketAddressInfo> peerListAnswer = new ArrayList<InetSocketAddressInfo>();
        if (peerMap == null) {
            return peerListAnswer;
        }
        Object[] peerList = peerMap.values().toArray();
        int lastPeer = peerList.length - 1;
        while (peerListAnswer.size() < missingNeighbors && lastPeer >= 0) {
            int randomPeer = random.nextInt(lastPeer + 1);
            Peer neighbor = (Peer) peerList[randomPeer];
            InetSocketAddressInfo address = neighbor.getAddress();
            if (!peer.neighbor(neighbor)) {
                peer.addNeighbor(neighbor);
                neighbor.addNeighbor(peer);
                peerListAnswer.add(neighbor.getAddress());
            }
            peerList[randomPeer] = peerList[lastPeer];
            peerList[lastPeer] = neighbor;
            lastPeer--;
        }
        return peerListAnswer;
    }
