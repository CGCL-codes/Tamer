    private PipeAdvertisement createAdvertisement(String digestString) {
        PipeAdvertisement pipeAdv = (PipeAdvertisement) AdvertisementFactory.newAdvertisement(PipeAdvertisement.getAdvertisementType());
        try {
            byte[] bid = MessageDigest.getInstance("MD5").digest(digestString.getBytes("ISO-8859-1"));
            PipeID pipeID = IDFactory.newPipeID(this.myNetwork.getAppPeerGroup().getPeerGroupID(), bid);
            pipeAdv.setPipeID(pipeID);
            pipeAdv.setType(PipeService.UnicastType);
            pipeAdv.setName("Socket pipe");
            pipeAdv.setDescription("verbose description");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
        return pipeAdv;
    }
