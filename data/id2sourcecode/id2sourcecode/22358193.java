    @SuppressWarnings("unchecked")
    private String makeHash(String request) throws CacheException {
        RequestCtx reqCtx = null;
        try {
            reqCtx = contextUtil.makeRequestCtx(request);
        } catch (MelcoeXacmlException pe) {
            throw new CacheException("Error converting request", pe);
        }
        digest.reset();
        Set<Attribute> attributes = null;
        Set<Subject> subjects = new TreeSet(new SubjectComparator());
        subjects.addAll(reqCtx.getSubjects());
        for (Subject s : subjects) {
            attributes = new TreeSet(new AttributeComparator());
            attributes.addAll(s.getAttributes());
            for (Attribute a : attributes) {
                hashAttribute(a);
            }
        }
        attributes = new TreeSet(new AttributeComparator());
        attributes.addAll(reqCtx.getResource());
        for (Attribute a : attributes) {
            hashAttribute(a);
        }
        attributes = new TreeSet(new AttributeComparator());
        attributes.addAll(reqCtx.getAction());
        for (Attribute a : attributes) {
            hashAttribute(a);
        }
        attributes = new TreeSet(new AttributeComparator());
        attributes.addAll(reqCtx.getEnvironmentAttributes());
        for (Attribute a : attributes) {
            hashAttribute(a);
        }
        byte[] hash = digest.digest();
        return byte2hex(hash);
    }
