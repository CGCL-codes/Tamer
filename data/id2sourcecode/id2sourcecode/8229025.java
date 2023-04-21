    public void tag_digest(RewriteContext hr) {
        String name = hr.get("name");
        String value = hr.get("value");
        debug(hr);
        hr.killToken();
        if (digest != null && name != null && value != null) {
            hr.request.props.put(name, Base64.encode(digest.digest(value.getBytes())));
        } else {
            debug(hr, "Invalid parameters or no digest available");
        }
    }
