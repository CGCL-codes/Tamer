    private void setupSocket(String digestString) {
        propagatedPipeAdv = (PipeAdvertisement) AdvertisementFactory.newAdvertisement(PipeAdvertisement.getAdvertisementType());
        try {
            byte[] bid = MessageDigest.getInstance("MD5").digest(digestString.getBytes("ISO-8859-1"));
            PipeID pipeID = IDFactory.newPipeID(peerGroup.getPeerGroupID(), bid);
            propagatedPipeAdv.setPipeID(pipeID);
            propagatedPipeAdv.setType(PipeService.PropagateType);
            propagatedPipeAdv.setName("The multicastsocket pipe");
            propagatedPipeAdv.setDescription("verbose description");
            System.out.println("Creating JxtaMulticastSocket");
            multicastSocketServer = new JxtaMulticastSocket(peerGroup, propagatedPipeAdv);
            multicastSocketServer.setSoTimeout(0);
            System.out.println("LocalAddress :" + multicastSocketServer.getLocalAddress());
            System.out.println("LocalSocketAddress :" + multicastSocketServer.getLocalSocketAddress());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
