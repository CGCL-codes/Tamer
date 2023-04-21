    public void put(String name, Scriptable start, Object value) {
        if (null == url || null != _din || null != _uu_out) super.put(name, start, value); else {
            if (null == _uu) {
                try {
                    _uu = url.openConnection();
                    try {
                        if (null != super._loc) _uu.setRequestProperty("Accept-Language", jsGet_locale().toString());
                    } catch (JavaScriptException jsx) {
                    }
                } catch (IOException iox) {
                    throw new IllegalStateException("Connection failed, cannot assign request headers.");
                }
            }
            _uu.setRequestProperty(name, ScriptRuntime.toString(value));
        }
    }
