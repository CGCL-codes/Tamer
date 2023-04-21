    public Filter readURL(InputStream is, ParsedURL purl, ICCColorSpaceExt colorSpace, boolean allowOpenStream, boolean returnBrokenLink) {
        if ((is != null) && !is.markSupported()) is = new BufferedInputStream(is);
        boolean needRawData = (colorSpace != null);
        Filter ret = null;
        URLImageCache cache = null;
        if (purl != null) {
            if (needRawData) cache = rawCache; else cache = imgCache;
            ret = cache.request(purl);
            if (ret != null) {
                if (colorSpace != null) ret = new ProfileRable(ret, colorSpace);
                return ret;
            }
        }
        boolean openFailed = false;
        List mimeTypes = getRegisteredMimeTypes();
        Iterator i;
        i = entries.iterator();
        while (i.hasNext()) {
            RegistryEntry re = (RegistryEntry) i.next();
            if (re instanceof URLRegistryEntry) {
                if ((purl == null) || !allowOpenStream) continue;
                URLRegistryEntry ure = (URLRegistryEntry) re;
                if (ure.isCompatibleURL(purl)) {
                    ret = ure.handleURL(purl, needRawData);
                    if (ret != null) break;
                }
                continue;
            }
            if (re instanceof StreamRegistryEntry) {
                StreamRegistryEntry sre = (StreamRegistryEntry) re;
                if (openFailed) continue;
                try {
                    if (is == null) {
                        if ((purl == null) || !allowOpenStream) break;
                        try {
                            is = purl.openStream(mimeTypes.iterator());
                        } catch (IOException ioe) {
                            openFailed = true;
                            continue;
                        }
                        if (!is.markSupported()) is = new BufferedInputStream(is);
                    }
                    if (sre.isCompatibleStream(is)) {
                        ret = sre.handleStream(is, purl, needRawData);
                        if (ret != null) break;
                    }
                } catch (StreamCorruptedException sce) {
                    is = null;
                }
                continue;
            }
        }
        if (cache != null) cache.put(purl, ret);
        if (ret == null) {
            if (!returnBrokenLink) return null;
            if (openFailed) return getBrokenLinkImage(this, ERR_URL_UNREACHABLE, new Object[] { purl });
            return getBrokenLinkImage(this, ERR_URL_UNINTERPRETABLE, new Object[] { purl });
        }
        if (ret.getProperty(BrokenLinkProvider.BROKEN_LINK_PROPERTY) != null) {
            return (returnBrokenLink) ? ret : null;
        }
        if (colorSpace != null) ret = new ProfileRable(ret, colorSpace);
        return ret;
    }
