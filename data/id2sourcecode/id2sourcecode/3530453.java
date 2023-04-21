    KnowledgeBase loadKnowledgeBase(RuleSource ruleSource) throws IOException, ClassNotFoundException, SecurityException {
        RedSecurityManager.checkUrl(ruleSource);
        BufferedInputStream inStream = new BufferedInputStream(getInputStream(ruleSource.getKnowledgeBaseLocation()));
        StringBuffer inString = new StringBuffer();
        ByteArrayOutputStream holdStream = new ByteArrayOutputStream();
        while (inStream.available() != 0) {
            holdStream.write(inStream.read());
        }
        byte[] base64Bytes = holdStream.toByteArray();
        byte[] binaryBytes = Base64.decodeBase64(base64Bytes);
        DroolsObjectInputStream in = new DroolsObjectInputStream(new ByteArrayInputStream(binaryBytes));
        Object inObject = in.readObject();
        log.info("inObject:" + inObject.getClass());
        return (KnowledgeBase) inObject;
    }
