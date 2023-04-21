        public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
            URL url = (publicId == null) ? null : (URL) _publicIDs.get(publicId);
            if (url == null) url = _systemIDs.get(systemId);
            if (url == null) return null;
            return new InputSource(url.openStream());
        }
