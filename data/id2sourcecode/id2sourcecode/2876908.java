    private void determineLastModifiedInfo() {
        lastModifiedDate = new Date(requestedFile.lastModified());
        lastModifiedDateString = Response.makeStandardHttpDateFormat().format(lastModifiedDate);
        try {
            lastModifiedDate = Response.makeStandardHttpDateFormat().parse(lastModifiedDateString);
        } catch (java.text.ParseException jtpe) {
            jtpe.printStackTrace();
        }
    }
