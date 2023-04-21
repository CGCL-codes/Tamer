    Reader openStream() throws IOException {
        if (url != null) {
            return new BufferedReader(new InputStreamReader(url.openStream()));
        } else {
            return new BufferedReader(new FileReader(path));
        }
    }
