    @Override
    public Result sendSMS(String number, String text, Proxy proxy) {
        try {
            DefaultHttpClient client = new DefaultHttpClient();
            if (proxy != null) {
                HttpHost prox = new HttpHost(proxy.host, proxy.port);
                client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, prox);
            }
            String target = "http://www.mobilfunk-talk.de/sms/";
            HttpGet get = new HttpGet(target);
            HttpResponse response = client.execute(get);
            HttpEntity e = response.getEntity();
            Document doc = ref.getDocumentFromInputStream(e.getContent());
            List<Element> forms = ref.selectByXPathOnDocument(doc, "//<ns>FORM", doc.getRootElement().getNamespaceURI());
            if (forms.size() == 0) return new Result(Result.SMS_LIMIT_REACHED);
            Element form = forms.get(0);
            List<NameValuePair> formparas = new ArrayList<NameValuePair>();
            List<Element> inputs = ref.selectByXPathOnElement(form, "//<ns>INPUT|//<ns>TEXTAREA|//<ns>SELECT", form.getNamespaceURI());
            Iterator<Element> it = inputs.iterator();
            while (it.hasNext()) {
                Element input = it.next();
                String type = input.getAttributeValue("type");
                String name = input.getAttributeValue("name");
                String value = input.getAttributeValue("value");
                if (type != null && type.equals("hidden")) {
                    formparas.add(new BasicNameValuePair(name, value));
                } else if (name != null && name.equals(FORM_NUMBER)) {
                    formparas.add(new BasicNameValuePair(name, this.getNumberPart(number)));
                } else if (name != null && name.equals(FORM_TEXT)) {
                    formparas.add(new BasicNameValuePair(name, text));
                }
            }
            formparas.add(new BasicNameValuePair("netz", this.getPrefixPart(number)));
            formparas.add(new BasicNameValuePair("counter", "120"));
            formparas.add(new BasicNameValuePair("agbcheck", "on"));
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparas, "UTF-8");
            HttpPost post = new HttpPost(target);
            post.setEntity(entity);
            response = client.execute(post);
            e = response.getEntity();
            doc = ref.getDocumentFromInputStream(e.getContent());
            List<Element> fonts = ref.selectByXPathOnDocument(doc, "//<ns>FONT[@color='#ff0000']", doc.getRootElement().getNamespaceURI());
            Iterator<Element> it2 = fonts.iterator();
            while (it2.hasNext()) {
                Element font = it2.next();
                String txt = font.getText();
                if (txt.contains("Bitte warten Sie bis morgen!")) {
                    return new Result(Result.SMS_LIMIT_REACHED);
                } else if (txt.contains("SMS Erfolgreich versendet!")) {
                    return new Result(Result.SMS_SEND);
                } else if (txt.contains("Fehler: Handynummer ung�ltig.")) {
                    return new Result(Result.NUMBER_NOT_VALID);
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
        return new Result(Result.UNKNOWN_ERROR);
    }
