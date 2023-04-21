    @Override
    public InputStream download(String path) throws RequestException {
        URLConnection urlConnection;
        BufferedInputStream in;
        ByteArrayOutputStream out;
        int b;
        try {
            urlConnection = new URL(urlSpec + path).openConnection();
            in = new BufferedInputStream(urlConnection.getInputStream());
            out = new ByteArrayOutputStream();
            while ((b = in.read()) != -1) {
                out.write(b);
            }
            return new ByteArrayInputStream(out.toByteArray());
        } catch (MalformedURLException e) {
            throw new RequestException("Bad server address.");
        } catch (IOException e) {
            throw new RequestException("Error reading file from server.");
        }
    }
