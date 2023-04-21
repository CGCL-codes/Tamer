    private void getURLContent() {
        try {
            URL url = new URL(this.url);
            Properties props = System.getProperties();
            props.put("http.proxyHost", this.proxyHost);
            props.put("http.proxyPort", this.proxyPort);
            URLConnection connection = url.openConnection();
            InputStream stream = connection.getInputStream();
            BufferedReader input = new BufferedReader(new InputStreamReader(stream));
            String line = "";
            while ((line = input.readLine()) != null) {
                if (atOnce) {
                    buffer.append(line + "\n");
                } else {
                    this.fireActionPerformed(new ActionEvent(this, ACTION_EVENT_LINE, line + "\n"));
                }
            }
            if (atOnce) this.fireActionPerformed(new ActionEvent(this, ACTION_EVENT_LINE, buffer.toString()));
        } catch (Exception e) {
            this.fireActionPerformed(new ActionEvent(this, ACTION_EVENT_ERROR, e.toString()));
        }
        this.fireActionPerformed(new ActionEvent(this, ACTION_EVENT_FINISHED, ""));
    }
