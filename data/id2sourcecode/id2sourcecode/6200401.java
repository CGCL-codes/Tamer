    private String getText(URL url) throws IOException {
        StringBuilder buf = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
        boolean start = true;
        while (true) {
            String line = reader.readLine();
            if (line != null) {
                if (!start) {
                    buf.append('\n');
                }
                buf.append(line);
                start = false;
            } else {
                break;
            }
        }
        reader.close();
        return buf.toString();
    }
