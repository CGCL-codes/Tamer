    public boolean save() {
        URL src = this.src;
        if (null != src) {
            String string = this.getText();
            if (null != string) {
                try {
                    byte[] utf = null;
                    int len = 0;
                    {
                        ByteArrayOutputStream buf = new ByteArrayOutputStream();
                        Writer writer = new OutputStreamWriter(buf, UTF8);
                        writer.write(string);
                        writer.flush();
                        utf = buf.toByteArray();
                        len = ((null != utf) ? (utf.length) : (0));
                    }
                    URLConnection urlc = src.openConnection();
                    if (urlc instanceof HttpURLConnection) {
                        HttpURLConnection http = (HttpURLConnection) urlc;
                        http.setRequestMethod("PUT");
                        http.setRequestProperty("Content-Type", "text/plain;charset=utf-8");
                        http.setRequestProperty("Content-Length", String.valueOf(len));
                    }
                    UrlcOutputStream out = this.writer(this, urlc);
                    try {
                        out.write(utf, 0, len);
                    } finally {
                        out.close();
                    }
                    if (out.isNotOk()) this.alertBroken("Save failed");
                    this.invalidate();
                    return true;
                } catch (IOException exc) {
                    exc.printStackTrace();
                    this.alertBroken(exc);
                }
            }
        }
        return false;
    }
