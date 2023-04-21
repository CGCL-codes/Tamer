        int getContentLength() throws IOException {
            return _url.openConnection().getContentLength();
        }
