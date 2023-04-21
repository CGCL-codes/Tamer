    public String sign(Map<String, String> attributes) throws UnsupportedEncodingException {
        _digest.reset();
        _digest.update(_secret.getBytes(UTF_8));
        List<String> flatAttributes = new ArrayList<String>(attributes.size());
        for (Iterator<Entry<String, String>> i = attributes.entrySet().iterator(); i.hasNext(); ) {
            Entry<String, String> entry = i.next();
            flatAttributes.add(entry.getKey() + entry.getValue());
        }
        Collections.sort(flatAttributes);
        for (String param : flatAttributes) _digest.update(param.getBytes(UTF_8));
        return convertToHex(_digest.digest());
    }
