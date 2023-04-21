    @Override
    public int run(List<XValue> args) throws Exception {
        Options opts = new Options("get:,put:,post:,head:,options:,delete:,connectTimeout:,contentType:,readTimeout:,+useCaches,+followRedirects,user:,password:,H=add-header:+,disableTrust:,keystore:,keypass:,sslproto:");
        opts.parse(args);
        SerializeOpts serializeOpts = getSerializeOpts();
        String method = "GET";
        boolean doInput = true;
        boolean doOutput = false;
        String surl = null;
        if (opts.hasOpt("get")) {
            method = "GET";
            surl = opts.getOptString("get", null);
        } else if (opts.hasOpt("put")) {
            method = "PUT";
            doInput = true;
            doOutput = true;
            surl = opts.getOptString("put", null);
        } else if (opts.hasOpt("post")) {
            method = "POST";
            doOutput = true;
            surl = opts.getOptString("post", null);
        } else if (opts.hasOpt("head")) {
            method = "HEAD";
            surl = opts.getOptString("head", null);
        } else if (opts.hasOpt("options")) {
            surl = opts.getOptString("options", null);
            method = "OPTIONS";
        } else if (opts.hasOpt("delete")) {
            surl = opts.getOptString("delete", null);
            method = "DELETE";
        } else if (opts.hasOpt("trace")) {
            method = "TRACE";
            surl = opts.getOptString("trace", null);
        } else surl = opts.getRemainingArgs().get(0).toString();
        if (surl == null) {
            usage();
            return 1;
        }
        int ret = 0;
        URL url = new URL(surl);
        URLConnection conn = url.openConnection();
        if (conn instanceof HttpURLConnection) {
            HttpURLConnection http = (HttpURLConnection) conn;
            setOptions(http, opts);
            http.setRequestMethod(method);
            OptionValue headers = opts.getOpt("H");
            if (headers != null) {
                for (XValue v : headers.getValues()) {
                    StringPair pair = new StringPair(v.toString(), '=');
                    http.addRequestProperty(pair.getLeft(), pair.getRight());
                }
            }
            http.setDoInput(doInput);
            http.setDoOutput(doOutput);
            if (doOutput) {
                conn.connect();
                OutputStream out = http.getOutputStream();
                Util.copyStream(getStdin().asInputStream(serializeOpts), out);
                out.close();
            }
            ret = http.getResponseCode();
            if (ret == 200) ret = 0;
        }
        if (doInput) {
            InputStream in = conn.getInputStream();
            Util.copyStream(in, getStdout().asOutputStream(serializeOpts));
            in.close();
        }
        return ret;
    }
