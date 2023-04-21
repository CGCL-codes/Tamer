    private ContentName findFullName(ContentName name, String str) throws IOException {
        byte[] content = str.getBytes();
        Exclude e = null;
        for (; ; ) {
            Interest i = Interest.constructInterest(name, e, Interest.CHILD_SELECTOR_LEFT, null, null, null);
            Log.info("searching for {0} content {1}, exclude {2}", name, str, e);
            ContentObject co = getHandle.get(i, TIMEOUT);
            Assert.assertTrue(null != co);
            Log.info("got result {0} digest={1}", co, DataUtils.printHexBytes(co.digest()));
            if (DataUtils.arrayEquals(co.content(), content)) return co.fullName();
            byte[][] omissions = { co.digest() };
            if (e == null) e = new Exclude(omissions); else e.add(omissions);
        }
    }
