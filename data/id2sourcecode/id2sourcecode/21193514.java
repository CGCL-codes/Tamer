    public StreamSource toStreamSource(Object source, String encoding, boolean asReader) throws JSONSchemaException {
        encoding = getEncoding(encoding);
        if (source instanceof URL || source instanceof URI) {
            try {
                URL url = (source instanceof URL) ? (URL) source : ((URI) source).toURL();
                if (asReader) {
                    return new StreamSource(new InputStreamReader(url.openStream(), encoding));
                } else {
                    return new StreamSource(url.openStream());
                }
            } catch (MalformedURLException muex) {
                throw new JSONSchemaException("Failed to create stream source for URI '" + source + "'. Error: " + muex, muex);
            } catch (IOException ioex) {
                throw new JSONSchemaException("Failed to create stream source for URI/URL '" + source + "'. Error: " + ioex, ioex);
            }
        } else if (source instanceof String) {
            try {
                URL url = new URL((String) source);
                if (asReader) {
                    return new StreamSource(new InputStreamReader(url.openStream(), encoding));
                } else {
                    return new StreamSource(url.openStream());
                }
            } catch (MalformedURLException muex) {
                try {
                    if (asReader) {
                        return new StreamSource(Utilities.getReaderForResource((String) source), encoding);
                    } else {
                        return new StreamSource(Thread.currentThread().getContextClassLoader().getResourceAsStream((String) source));
                    }
                } catch (Throwable th) {
                    throw new JSONSchemaException("Failed to create stream source for resource '" + source + "'. Error: " + th, th);
                }
            } catch (IOException ioex) {
                throw new JSONSchemaException("Failed to create stream source for string '" + source + "'. Error: " + ioex, ioex);
            }
        } else if (source instanceof File && ((File) source).isFile()) {
            try {
                if (asReader) {
                    return new StreamSource(new InputStreamReader(new FileInputStream((File) source), encoding));
                } else {
                    return new StreamSource(new FileInputStream((File) source));
                }
            } catch (IOException ioex) {
                throw new JSONSchemaException("Failed to create stream source for file '" + source + "'. Error: " + ioex, ioex);
            }
        } else {
            throw new JSONSchemaException("Failed to create stream source for " + source);
        }
    }
