    public ErrorCode filter(AgentContext ctx) {
        ByteArrayOutputStream bos;
        MessageDigest md;
        AgentCard card;
        Resource resource;
        byte[] hash;
        try {
            card = (AgentCard) ctx.get(FieldType.CARD);
            if (card == null) {
                throw new NullPointerException("card");
            }
            resource = (Resource) ctx.get(FieldType.RESOURCE);
            if (resource == null) {
                throw new NullPointerException("resource");
            }
            bos = new ByteArrayOutputStream();
            Resources.zip(resource, new LinkedList(new TreeSet(resource.list())), bos);
            md = MessageDigest.getInstance(MESSAGE_DIGEST);
            hash = md.digest(bos.toByteArray());
            ctx.set(FieldType.ZIPPED_RESOURCE_HASH, hash);
            log_.info("Hash code of zipped agent resource for agent '" + card + "': " + toHexString(hash));
        } catch (Exception e) {
            log_.caught(LogLevel.ERROR, "Could not compute hash code of the zipped agent resource", e);
        }
        return ErrorCode.OK;
    }
