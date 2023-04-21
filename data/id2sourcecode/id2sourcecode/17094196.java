    private boolean merge() {
        try {
            final SequenceInputStream stream = new SequenceInputStream(new ListOfFiles(this.streams));
            final MessageDigest digester = MessageDigest.getInstance("MD5");
            final OutputStream mergedOutput = new FileOutputStream(this.getMergedOutputFile());
            byte[] buffer = new byte[4096];
            int length = 0;
            while ((length = stream.read(buffer)) > 0) {
                digester.update(buffer, 0, buffer.length);
                mergedOutput.write(buffer, 0, length);
            }
            final String checksum = new BigInteger(1, digester.digest()).toString(16);
            try {
                final SAXBuilder builder = new SAXBuilder();
                final Document document = builder.build(this.getJcompressorContext().getHomePath() + "/resources.xml");
                final Element rootNode = document.getRootElement();
                final List<Object> templates = rootNode.getChildren("template");
                for (final Object templateObj : templates) {
                    final Element templateElement = (Element) templateObj;
                    if (StringUtils.equals("template", templateElement.getName())) {
                        final String uri = templateElement.getAttributeValue("uri");
                        System.out.println(uri);
                        if (StringUtils.equals(this.getJcompressorContext().getUri(), uri)) {
                            for (final Object resourceObj : templateElement.getChildren("resource")) {
                                final Element resourceElement = (Element) resourceObj;
                                System.out.println(resourceElement.getChildText("file"));
                                System.out.println(resourceElement.getChildText("checksum"));
                            }
                        }
                    }
                }
            } catch (JDOMException e) {
                e.printStackTrace();
            }
            stream.close();
            mergedOutput.close();
            return true;
        } catch (IOException e) {
            throw new JcompressorException("An error has occurred while concatenating file streams", e);
        } catch (NoSuchAlgorithmException e) {
            throw new JcompressorException(e);
        }
    }
