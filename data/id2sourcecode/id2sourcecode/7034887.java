    public void get(IMetaCollection aCollection) throws TransducerException {
        if (null != ioTransducer) {
            try {
                URL urlObj = new URL(url);
                URLConnection urlConn = urlObj.openConnection();
                InputStreamReader inr = new InputStreamReader(urlConn.getInputStream());
                ioTransducer.setReader(new BufferedReader(inr));
                ioTransducer.get(aCollection);
            } catch (Exception e) {
                throw new TransducerException(e);
            }
        } else {
            throw new TransducerException("An IIOTransducer instance must first be set on the URLTransducerAdapter.");
        }
    }
