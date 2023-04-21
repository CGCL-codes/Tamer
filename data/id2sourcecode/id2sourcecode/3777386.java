    JHighlightVersion() {
        URL version_url = getClass().getClassLoader().getResource("JHIGHLIGHT_VERSION");
        if (version_url != null) {
            try {
                URLConnection connection = version_url.openConnection();
                connection.setUseCaches(false);
                InputStream inputStream = connection.getInputStream();
                byte[] buffer = new byte[64];
                int return_value = -1;
                ByteArrayOutputStream output_stream = new ByteArrayOutputStream(buffer.length);
                try {
                    return_value = inputStream.read(buffer);
                    while (-1 != return_value) {
                        output_stream.write(buffer, 0, return_value);
                        return_value = inputStream.read(buffer);
                    }
                } finally {
                    output_stream.close();
                    inputStream.close();
                }
                mVersion = output_stream.toString("UTF-8");
            } catch (IOException e) {
                mVersion = null;
            }
        }
        if (mVersion != null) {
            mVersion = mVersion.trim();
        }
        if (null == mVersion) {
            mVersion = "(unknown version)";
        }
    }
