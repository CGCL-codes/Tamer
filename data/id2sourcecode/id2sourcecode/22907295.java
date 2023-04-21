        public AttachmentInfo(String href, String contentType, String location) throws IOException {
            this.contentType = contentType;
            this.location = location;
            if (contentType == null) {
                file = new File(href);
            } else if (contentType.equalsIgnoreCase("text/xml")) {
                content = new StreamSource(href);
            } else if (contentType.equalsIgnoreCase("text/plain")) {
                Reader reader = new BufferedReader(new FileReader(href));
                StringWriter writer = new StringWriter();
                while (reader.ready()) {
                    writer.write(reader.read());
                }
                content = writer.toString();
            } else {
                content = new FileInputStream(href);
            }
        }
