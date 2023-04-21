    public void generate(Buffer out) {
        BufferedReader br = null;
        try {
            String url = Url.createURL(this.url, contextURI, context);
            String query = Url.getQueryString(children, context);
            if (query.length() > 0) url += query;
            if (!Util.isAbsoluteURL(url)) {
                StringBuffer buf = ((Request) context.getRequest()).getServerBaseURL();
                buf.append(url);
                url = buf.toString();
            }
            if (charEncoding != null) {
                br = new BufferedReader(new InputStreamReader(new URL(url).openStream(), charEncoding));
            } else {
                br = new BufferedReader(new InputStreamReader(new URL(url).openStream()));
            }
            int scope = Context.getScopeFromName(this.scope);
            if (var != null) {
                StringBuffer buf = new StringBuffer();
                int c;
                while ((c = br.read()) > -1) {
                    buf.append((char) c);
                }
                context.setAttribute(var, buf.toString());
            } else {
                if (varReader == null) throw new IllegalStateException("Neither var nor varName attribute set.");
                context.setAttribute(var, br);
            }
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException("error reading resourse reader", e);
        } finally {
            try {
                if (br != null) br.close();
            } catch (IOException e) {
                throw new RuntimeException("Error closing resourse reader", e);
            }
        }
    }
