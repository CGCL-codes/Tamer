    @Override
    public Object getContent(IMedium medium) throws MM4UCannotReadMediumElementsContentException {
        String tempURL = medium.getURI();
        try {
            URL url = new URL(tempURL);
            InputStream tempInputStream = url.openStream();
            Properties twmProperties = new Properties();
            twmProperties.load(tempInputStream);
            String content = twmProperties.getProperty("text");
            if (content == null) {
                tempInputStream.close();
                tempInputStream = url.openStream();
                content = "";
                byte[] temp = new byte[1024];
                int length = -1;
                while ((length = tempInputStream.read(temp)) != -1) content += new String(temp, 0, length);
            }
            tempInputStream.close();
            tempInputStream = null;
            return content;
        } catch (IOException exception) {
            throw new MM4UCannotReadMediumElementsContentException(this, "getContent", "Can not read medium content from given connector medium url: " + tempURL);
        }
    }
