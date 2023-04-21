    protected void request() throws Exception {
        cal.clear();
        contract = getCurrentContract();
        URL url = new URL(contract.getUrlString() + contract.getSymbol() + ".day");
        setInputStream(url.openStream());
    }
