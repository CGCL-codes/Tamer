    public ThirdPartySearchResult(String name, String type, String url, int size, long creationTime, LimeXMLDocument xmlDoc, String vendor, String keyword) {
        this.name = name;
        this.url = url;
        this.size = size;
        this.creationTime = creationTime;
        this.xmlDoc = xmlDoc;
        this.vendor = vendor;
        this.type = type == null ? "" : type;
        this.keyword = keyword;
        URN tmpUrn = null;
        try {
            SHA1 md1 = new SHA1();
            if (name != null) md1.update(name.getBytes("UTF-8"));
            if (url != null) md1.update(url.getBytes("UTF-8"));
            if (vendor != null) md1.update(vendor.getBytes("UTF-8"));
            String urnString = new String(Base32.encode(md1.digest()));
            if (urnString.length() > 20) urnString = urnString.substring(0, 20);
            tmpUrn = URN.createSHA1UrnFromBytes(urnString.getBytes());
        } catch (UnsupportedEncodingException e) {
            LOG.error(e);
        } catch (IOException e) {
            LOG.error(e);
        }
        this.urn = tmpUrn;
    }
