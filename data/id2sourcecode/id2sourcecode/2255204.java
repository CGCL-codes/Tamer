    public OutputStream getOutputStream() {
        if (null != _uu_out) return _uu_out; else {
            if (null == url) return null; else if (null == _uu) {
                try {
                    _uu = url.openConnection();
                    _uu.setAllowUserInteraction(false);
                    _uu.setDoOutput(true);
                    try {
                        if (null != super._loc) _uu.setRequestProperty("Accept-Language", jsGet_locale());
                    } catch (JavaScriptException jsx) {
                    }
                } catch (IOException iox) {
                    return null;
                }
            }
            if (null != _din) throw new IllegalStateException("A URL transaction is not valid for output following input.  Please close the connection, then do output."); else if (null == _uu_out) {
                try {
                    _uu_out = new UrlcOutputStream(this, _uu.getOutputStream());
                } catch (IOException iox) {
                    return null;
                }
            }
            return _uu_out;
        }
    }
