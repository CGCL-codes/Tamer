    public URLConnection openConnection(URL u) {
        URL magnet_url;
        try {
            String str = u.toString();
            str = str.substring(6);
            int param_pos = str.indexOf('/');
            String hash = param_pos == -1 ? str : str.substring(0, param_pos);
            hash = hash.trim();
            int dot_pos = hash.indexOf('.');
            if (dot_pos != -1) {
                hash = hash.substring(0, dot_pos).trim();
            }
            if (hash.length() == 40) {
                hash = Base32.encode(ByteFormatter.decodeString(hash));
            }
            magnet_url = new URL("magnet:?xt=urn:btih:" + hash + "/" + (param_pos == -1 ? "" : str.substring(param_pos + 1)));
        } catch (Throwable e) {
            Debug.out("Failed to transform dht url '" + u + "'", e);
            return (null);
        }
        try {
            return (magnet_url.openConnection());
        } catch (MalformedURLException e) {
            Debug.printStackTrace(e);
            return (null);
        } catch (IOException e) {
            Debug.printStackTrace(e);
            return (null);
        }
    }
