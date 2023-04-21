    public void init(String uri) {
        ElvisListModel model = new ElvisListModel();
        this.jcbInstances.setModel(model);
        try {
            URL urlListResources = new URL(ElvisRegistry.getInstance().getProperty("elvis.server") + "/servlet/listResources?xpath=document()//Book");
            InputStream streamResources = urlListResources.openStream();
            XmlPullParser xpp = XmlPullParserFactory.newInstance().newPullParser();
            xpp.setInput(new InputStreamReader(streamResources));
            int type = xpp.getEventType();
            ElvisBookResource selectEr = null;
            while (type != XmlPullParser.END_DOCUMENT) {
                if (type == XmlPullParser.START_TAG && "Resource".equals(xpp.getName())) {
                    ElvisBookResource er = model.add(xpp.getAttributeValue("", "resId"), xpp.getAttributeValue("", "author"), xpp.getAttributeValue("", "title"));
                    if (uri != null && uri.equals(er.getUri())) {
                        selectEr = er;
                    }
                }
                type = xpp.next();
            }
            if (selectEr != null) {
                this.jcbInstances.setSelectedItem(selectEr);
                this.jcbInstances.setEnabled(false);
            }
            streamResources.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (XmlPullParserException xppe) {
            xppe.printStackTrace();
        }
    }
