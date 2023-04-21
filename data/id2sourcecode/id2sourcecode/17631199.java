            @Override
            protected InputStream toInputStream() throws IOException {
                URLConnection connection = url.openConnection();
                connection.setAllowUserInteraction(false);
                connection.connect();
                if (connection.getContentLength() == -1) {
                    try {
                        byte[] bytes = null;
                        bytes = readInputStream(connection.getInputStream(), bytes);
                        length = bytes.length;
                        return new ByteArrayInputStream(bytes);
                    } finally {
                        connection.getInputStream().close();
                    }
                } else {
                    length = connection.getContentLength();
                    return connection.getInputStream();
                }
            }
