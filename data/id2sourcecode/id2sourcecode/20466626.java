    @Override
    public void completed(HttpResponse message) {
        super.completed(message);
        if (message.getEntity().getContentType().getValue().equals("image/jpeg")) {
            try {
                ByteArrayOutputStream output = new ByteArrayOutputStream();
                ImageIO.write(ImageIO.read(message.getEntity().getContent()), "jpeg", output);
                TreeMap<String, byte[]> map = new TreeMap<String, byte[]>();
                map.put(bundle.getHeaders().get("Video-Stream").toString(), output.toByteArray());
                publisher.addToReadQueue(ProtocolHelper.unmarshel(map));
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
        }
    }
