    public Equipment(String ip, String name, String writeCommunity, String readCommunity, String port, String timeout, EquipmentStatus status, String retries) {
        setIP(ip);
        setName(name);
        setWriteCommunity(writeCommunity);
        setReadCommunity(readCommunity);
        setPort(port);
        setTimeout(timeout);
        setStatus(status);
        setRetries(retries);
    }
