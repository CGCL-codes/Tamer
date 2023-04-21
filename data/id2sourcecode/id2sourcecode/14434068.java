            @Override
            protected Reader openConnection(URL url) throws IOException {
                URLConnection connection = url.openConnection();
                connection.addRequestProperty("User-Agent", "Mozilla");
                return getReader(connection);
            }
