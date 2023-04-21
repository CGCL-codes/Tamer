    private void onReceiveControlAnswer(IEnvelope env, ControlIntroductionAnswer answer) {
        Logger.log(answer.getSenderPeerID() + " confirmed that he is our relay host!");
        relayPeers.put(answer.getSenderPeerID(), (SocketChannel) env.getChannel());
        System.out.println(answer.getSenderPeerID() + " confirmed that he is our relay host!!");
    }
