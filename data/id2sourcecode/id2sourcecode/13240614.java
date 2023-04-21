    public Representation getResponseEntity() {
        Representation result = null;
        if (getResponseStream() != null) {
            result = new InputRepresentation(getResponseStream(), null);
        } else if (getResponseChannel() != null) {
            result = new ReadableRepresentation(getResponseChannel(), null);
        } else if (getMethod().equals(Method.HEAD.getName())) {
            result = new Representation() {

                @Override
                public ReadableByteChannel getChannel() throws IOException {
                    return null;
                }

                @Override
                public InputStream getStream() throws IOException {
                    return null;
                }

                @Override
                public void write(OutputStream outputStream) throws IOException {
                }

                @Override
                public void write(WritableByteChannel writableChannel) throws IOException {
                }
            };
        }
        if (result != null) {
            for (Parameter header : getResponseHeaders()) {
                if (header.getName().equalsIgnoreCase(HttpConstants.HEADER_CONTENT_TYPE)) {
                    ContentType contentType = new ContentType(header.getValue());
                    if (contentType != null) {
                        result.setMediaType(contentType.getMediaType());
                        result.setCharacterSet(contentType.getCharacterSet());
                    }
                } else if (header.getName().equalsIgnoreCase(HttpConstants.HEADER_CONTENT_LENGTH)) {
                    result.setSize(Long.parseLong(header.getValue()));
                } else if (header.getName().equalsIgnoreCase(HttpConstants.HEADER_EXPIRES)) {
                    result.setExpirationDate(parseDate(header.getValue(), false));
                } else if (header.getName().equalsIgnoreCase(HttpConstants.HEADER_CONTENT_ENCODING)) {
                    HeaderReader hr = new HeaderReader(header.getValue());
                    String value = hr.readValue();
                    while (value != null) {
                        Encoding encoding = new Encoding(value);
                        if (!encoding.equals(Encoding.IDENTITY)) {
                            result.getEncodings().add(encoding);
                        }
                        value = hr.readValue();
                    }
                } else if (header.getName().equalsIgnoreCase(HttpConstants.HEADER_CONTENT_LANGUAGE)) {
                    HeaderReader hr = new HeaderReader(header.getValue());
                    String value = hr.readValue();
                    while (value != null) {
                        result.getLanguages().add(new Language(value));
                        value = hr.readValue();
                    }
                } else if (header.getName().equalsIgnoreCase(HttpConstants.HEADER_LAST_MODIFIED)) {
                    result.setModificationDate(parseDate(header.getValue(), false));
                } else if (header.getName().equalsIgnoreCase(HttpConstants.HEADER_ETAG)) {
                    result.setTag(Tag.parse(header.getValue()));
                } else if (header.getName().equalsIgnoreCase(HttpConstants.HEADER_CONTENT_LOCATION)) {
                    result.setIdentifier(header.getValue());
                }
            }
        }
        return result;
    }
