    private boolean uploadOneGallery(Gallery g) throws FBConnectionException, FBErrorException, MalformedURLException, IOException {
        URL url = new URL(getHost() + getPath());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestProperty("X-FB-User", getUser());
        conn.setRequestProperty("X-FB-Auth", makeResponse());
        conn.setRequestProperty("X-FB-Mode", "CreateGals");
        conn.setRequestProperty("X-FB-CreateGals.Gallery._size", "1");
        if (!gallery.isTopLevel()) {
            conn.setRequestProperty("X-FB-CreateGals.Gallery.0.ParentID", Integer.toString(g.getIdentifiedParent()));
        }
        conn.setRequestProperty("X-FB-CreateGals.Gallery.0.GalName", g.getName());
        conn.setRequestProperty("X-FB-CreateGals.Gallery.0.GalSec", Integer.toString(g.getSecurity()));
        if (!gallery.isUndated()) {
            conn.setRequestProperty("X-FB-CreateGals.Gallery.0.GalDate", gallery.getDate());
        }
        conn.connect();
        Element fbresponse;
        try {
            fbresponse = readXML(conn);
        } catch (FBConnectionException fbce) {
            throw fbce;
        } catch (FBErrorException fbee) {
            throw fbee;
        } catch (Exception e) {
            FBConnectionException fbce = new FBConnectionException("XML parsing failed");
            fbce.attachSubException(e);
            throw fbce;
        }
        NodeList gal = fbresponse.getElementsByTagName("CreateGalsResponse");
        for (int i = 0; i < gal.getLength(); i++) {
            Element curelement = (Element) gal.item(i);
            if (hasError(curelement)) {
                FBErrorException fbee = new FBErrorException();
                fbee.setErrorCode(errorcode);
                fbee.setErrorText(errortext);
                throw fbee;
            }
        }
        gal = fbresponse.getElementsByTagName("Gallery");
        for (int i = 0; i < gal.getLength(); i++) {
            Element curelement = (Element) gal.item(i);
            if (hasError(curelement)) {
                FBErrorException fbee = new FBErrorException();
                fbee.setErrorCode(errorcode);
                fbee.setErrorText(errortext);
                throw fbee;
            }
            try {
                g.setURL(DOMUtil.getSimpleElementText(curelement, "GalURL"));
                g.setName(DOMUtil.getSimpleElementText(curelement, "GalName"));
                g.setID(Integer.parseInt(DOMUtil.getSimpleElementText(curelement, "GalID")));
            } catch (Exception e) {
                System.out.println("HEY!  Metadata failed to parse on gallery " + g.getName() + "!");
            }
        }
        conn.disconnect();
        return true;
    }
