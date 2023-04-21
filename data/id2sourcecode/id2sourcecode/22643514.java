    public ImagePlus openURL(String url) {
        try {
            String name = "";
            int index = url.lastIndexOf('/');
            if (index == -1) index = url.lastIndexOf('\\');
            if (index > 0) name = url.substring(index + 1); else throw new MalformedURLException("Invalid URL: " + url);
            if (url.indexOf(" ") != -1) url = url.replaceAll(" ", "%20");
            URL u = new URL(url);
            IJ.showStatus("" + url);
            String lurl = url.toLowerCase(Locale.US);
            ImagePlus imp = null;
            if (lurl.endsWith(".tif")) imp = openTiff(u.openStream(), name); else if (lurl.endsWith(".zip")) imp = openZipUsingUrl(u); else if (lurl.endsWith(".jpg") || lurl.endsWith(".gif")) imp = openJpegOrGifUsingURL(name, u); else if (lurl.endsWith(".dcm") || lurl.endsWith(".ima")) {
                imp = (ImagePlus) IJ.runPlugIn("ij.plugin.DICOM", url);
                if (imp != null && imp.getWidth() == 0) imp = null;
            } else if (lurl.endsWith(".png")) imp = openPngUsingURL(name, u); else {
                URLConnection uc = u.openConnection();
                String type = uc.getContentType();
                if (type != null && (type.equals("image/jpeg") || type.equals("image/gif"))) imp = openJpegOrGifUsingURL(name, u); else if (type != null && type.equals("image/png")) imp = openPngUsingURL(name, u); else imp = openWithHandleExtraFileTypes(url, new int[] { 0 });
            }
            IJ.showStatus("");
            return imp;
        } catch (Exception e) {
            String msg = e.getMessage();
            if (msg == null || msg.equals("")) msg = "" + e;
            IJ.error("Open URL", msg + "\n \n" + url);
            return null;
        }
    }
