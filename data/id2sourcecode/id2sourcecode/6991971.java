    public void refresh() throws IOException {
        final URLConnection c = this.url.openConnection();
        final BufferedReader in = new BufferedReader(new InputStreamReader(c.getInputStream()));
        String inputLine;
        final StringBuffer cx = new StringBuffer();
        boolean isTheFirst = true;
        while ((inputLine = in.readLine()) != null) {
            if (!isTheFirst) {
                cx.append('\n');
            }
            cx.append(inputLine);
            isTheFirst = false;
        }
        in.close();
        this.content = cx.toString();
    }
