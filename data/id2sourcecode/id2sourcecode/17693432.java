    public void generateProxyList() {
        try {
            HttpClient client = new DefaultHttpClient();
            String target = "http://www.proxy-listen.de/Proxy/Proxyliste.html";
            HttpGet get = new HttpGet(target);
            HttpResponse response = client.execute(get);
            HttpEntity e = response.getEntity();
            List<NameValuePair> formparas = new ArrayList<NameValuePair>();
            Document doc = this.getDocumentFromInputStream(e.getContent());
            List<Element> forms = this.selectByXPathOnDocument(doc, "//<ns>FORM[<ns>INPUT[@type='hidden']]", doc.getRootElement().getNamespaceURI());
            Element form = forms.get(0);
            List<Element> hiddens = this.selectByXPathOnElement(form, "//<ns>INPUT[@type='hidden']", form.getNamespaceURI());
            Iterator<Element> it = hiddens.iterator();
            while (it.hasNext()) {
                Element hidden = it.next();
                String name = hidden.getAttributeValue("name");
                String value = hidden.getAttributeValue("value");
                formparas.add(new BasicNameValuePair(name, value));
            }
            formparas.add(new BasicNameValuePair("submit", "Anzeigen"));
            formparas.add(new BasicNameValuePair("type", "http"));
            formparas.add(new BasicNameValuePair("liststyle", "leech"));
            HttpPost post = new HttpPost(target);
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparas, "UTF-8");
            post.setEntity(entity);
            response = client.execute(post);
            e = response.getEntity();
            doc = this.getDocumentFromInputStream(e.getContent());
            List<Element> proxylist = this.selectByXPathOnDocument(doc, "//<ns>A[@class='proxyList']", doc.getRootElement().getNamespaceURI());
            Iterator<Element> it2 = proxylist.iterator();
            while (it2.hasNext()) {
                Element p = it2.next();
                String proxystring = p.getText();
                String[] split = proxystring.split(":");
                if (split.length == 2) {
                    String host = split[0];
                    int port = Integer.valueOf(split[1]);
                    Timer t = new Timer();
                    t.schedule(new CheckTask(this, port, host), 0);
                }
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (JDOMException e) {
            e.printStackTrace();
        }
    }
