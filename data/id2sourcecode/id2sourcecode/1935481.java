    public boolean connect() {
        if (this.debug) {
            String debugStr = this.urlStr.replaceAll("&Password=[^&]*", "&Password=xxx");
            System.out.println("Connect: " + debugStr);
        }
        try {
            this.url = new URL(this.urlStr);
            this.conn = this.url.openConnection();
            this.conn.setDoOutput(true);
            this.conn.setDoInput(true);
            this.inputStream = conn.getInputStream();
        } catch (Exception e) {
            return false;
        }
        return true;
    }
