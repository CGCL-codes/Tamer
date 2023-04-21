    protected void request() throws Exception {
        cal.clear();
        contract = getCurrentContract();
        if (contract.getInputStream() == null) {
            URL url = new URL(contract.getUrlString());
            setInputStream(url.openStream());
        } else {
            setInputStream(contract.getInputStream());
        }
    }
