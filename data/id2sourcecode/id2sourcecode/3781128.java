        private URLConnectionImpl() throws IOException {
            urlConnection = url.openConnection();
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.setRequestProperty("Content-Type", "text/xml");
        }
