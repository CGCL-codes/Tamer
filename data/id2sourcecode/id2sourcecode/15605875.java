    public String getContentType() {
        String contType = UNKNOWN_TYPE;
        if (candURI instanceof CrawlURI) {
            contType = ((CrawlURI) candURI).getContentType();
        }
        if (contType == null) {
            if (candURI.containsKey(SPEC_CONTENT_TYPE)) {
                contType = candURI.getString(SPEC_CONTENT_TYPE);
            } else {
                String uri = candURI.getUURI().toString();
                try {
                    URL url = new URL(uri);
                    contType = url.openConnection().getContentType();
                } catch (Exception e) {
                    e.printStackTrace();
                    contType = UNKNOWN_TYPE;
                }
                contType = (contType != null) ? contType : UNKNOWN_TYPE;
                candURI.putString(SPEC_CONTENT_TYPE, contType);
            }
        }
        return contType;
    }
