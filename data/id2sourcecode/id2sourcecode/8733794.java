    @Override
    public Result sendSMS(String number, String text, Proxy proxy) {
        try {
            DefaultHttpClient client = new DefaultHttpClient();
            if (proxy != null) {
                HttpHost prox = new HttpHost(proxy.host, proxy.port);
                client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, prox);
            }
            String target = "http://www.frei-simser.de/sms-ohne-anmeldung/";
            HttpGet get = new HttpGet(target);
            HttpResponse response = client.execute(get);
            HttpEntity e = response.getEntity();
            Document doc = ref.getDocumentFromInputStream(e.getContent());
            List<Element> captchas = ref.selectByXPathOnDocument(doc, "//<ns>IMG[@class='tx-srfreecap-pi2-image']", doc.getRootElement().getNamespaceURI());
            Element captcha = captchas.get(0);
            String url = captcha.getAttributeValue("src");
            HttpGet imgcall = new HttpGet(url);
            HttpResponse imgres = client.execute(imgcall);
            HttpEntity imge = imgres.getEntity();
            BufferedImage img = ImageIO.read(imge.getContent());
            imge.getContent().close();
            Icon icon = new ImageIcon(img);
            String result = (String) JOptionPane.showInputDialog(null, "Bitte Captcha eingeben:", "Captcha", JOptionPane.INFORMATION_MESSAGE, icon, null, "");
            List<NameValuePair> formparas = new ArrayList<NameValuePair>();
            formparas.add(new BasicNameValuePair(FORM_CAPTCHA, result));
            formparas.add(new BasicNameValuePair(FORM_EMAIL, "a@b.c"));
            formparas.add(new BasicNameValuePair("tx_epsmsgw_pi1[action]", "send"));
            formparas.add(new BasicNameValuePair("tx_epsmsgw_pi1[country]", "DE"));
            formparas.add(new BasicNameValuePair(FORM_TEXT, text));
            formparas.add(new BasicNameValuePair(FORM_NUMBER, this.getNumberPart(number)));
            formparas.add(new BasicNameValuePair(FORM_PREFIX, this.getPrefixPart(number)));
            URI request = URIUtils.createURI("http", "www.frei-simser.de", -1, "/sms-ohne-anmeldung/", URLEncodedUtils.format(formparas, "UTF-8"), null);
            get = new HttpGet(request);
            response = client.execute(get);
            e = response.getEntity();
            doc = ref.getDocumentFromInputStream(e.getContent());
            List<Element> fonts = ref.selectByXPathOnDocument(doc, "//<ns>H1", doc.getRootElement().getNamespaceURI());
            Iterator<Element> it2 = fonts.iterator();
            while (it2.hasNext()) {
                Element font = it2.next();
                String txt = font.getText();
                if (txt.contains("Free SMS Versand erfolgt")) {
                    return new Result(Result.SMS_SEND);
                } else if (txt.contains("Free SMS Versand gescheitert")) {
                    return new Result(Result.UNKNOWN_ERROR);
                }
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (JDOMException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return new Result(Result.UNKNOWN_ERROR);
    }
