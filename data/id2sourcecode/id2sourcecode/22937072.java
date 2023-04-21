    @Test
    public void testMatchDigest() throws MalformedContentNameStringException {
        ContentName name = ContentName.fromNative("/paul");
        byte[] content = "hello".getBytes();
        ContentObject co = ContentObject.buildContentObject(name, content);
        byte[] digest = co.digest();
        Interest interest = new Interest(ContentName.fromNative(name, digest));
        Assert.assertTrue(interest.matches(co));
        interest = new Interest(ContentName.fromNative(name, "simon"));
        Assert.assertFalse(interest.matches(co));
    }
