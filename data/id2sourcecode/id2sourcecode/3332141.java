    protected void readJavaScript(Parser parser, URL url) throws ParserException {
        try {
            NodeFilter[] nf = { new TagNameFilter("script"), new TagNameFilter("SCRIPT") };
            NodeList nlist = parser.extractAllNodesThatMatch(new OrFilter(nf));
            int length = nlist.size();
            for (int i = 0; i < length; i++) {
                Node node = nlist.elementAt(i);
                if (node instanceof Tag) {
                    Tag tag = (Tag) node;
                    if ("script".equals(tag.getTagName().toLowerCase())) {
                        String lin = "";
                        String external_js_path = "";
                        String script_text = "";
                        if ((lin = tag.getAttribute("src")) != null) {
                            if (lin.toLowerCase().startsWith("http") || lin.toLowerCase().startsWith("www")) {
                                external_js_path = lin;
                            } else {
                                String host = url.getHost();
                                int last_ind = lin.lastIndexOf("../");
                                String n_url = "";
                                if (last_ind != -1) {
                                    n_url = lin.substring(last_ind + 2);
                                } else if (!lin.startsWith("/")) {
                                    n_url = "/" + lin;
                                }
                                external_js_path = "http://" + host + n_url;
                            }
                            if (!FindHTMLTag.isNullEmptyString(external_js_path)) {
                                try {
                                    URL js_url = new URL(external_js_path);
                                    URLConnection con = js_url.openConnection();
                                    con.connect();
                                    BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                                    String line = null;
                                    while ((line = br.readLine()) != null) {
                                        script_text += (line + "\n");
                                    }
                                } catch (Exception e) {
                                }
                            }
                        } else {
                            script_text = tag.toPlainTextString().trim();
                        }
                        if (!FindHTMLTag.isNullEmptyString(script_text)) {
                            getJSFunctionBody(script_text);
                        }
                    }
                }
            }
        } catch (EncodingChangeException ece) {
            ece.printStackTrace();
            System.out.println("\n" + this.path);
        } catch (org.htmlparser.util.ParserException pe) {
            pe.printStackTrace();
            System.out.println("\n" + this.path);
        }
    }
