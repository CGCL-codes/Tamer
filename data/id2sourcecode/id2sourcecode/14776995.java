    @Test
    public final void testStoreAndRetrieve() throws DigitalObjectNotStoredException, URISyntaxException, DigitalObjectNotFoundException, IOException {
        System.out.println("Testing storage of Digital Object");
        URI purl = new File(AllStorageSuite.TEST_DATA_BASE, FILE).toURI();
        System.out.println("Creating DigitalObjectContent c1");
        DigitalObjectContent c1 = Content.byReference(purl.toURL().openStream());
        System.out.println("Creating DigitalObject object");
        DigitalObject object = new DigitalObject.Builder(c1).permanentUri(purl).title(purl.toString()).build();
        System.out.println("Setting store flag to true");
        boolean storeFlag = true;
        URI pdURI = null;
        try {
            System.out.println("calling this.dom.storeAsNew(object)");
            pdURI = DigitalObjectManagerTests.dom.storeAsNew(object);
            System.out.println("StoreAsNew returned the URI:" + pdURI);
        } catch (Exception e) {
            System.out.println("Caught an exception in storeAsNew, here's the details");
            e.printStackTrace();
            System.out.println("Asserting it's a not stored exception");
            assertTrue("Expecting exception to be DigitalObjectNotStoredException", e.getClass().equals(DigitalObjectNotStoredException.class));
            System.out.println("Setting storeFlag to false");
            storeFlag = false;
        } catch (Throwable t) {
            System.out.println("Caught a throwable in storeAsNew, here's the details");
            t.printStackTrace();
            System.out.println("Setting storeFlag to false");
            storeFlag = false;
        }
        System.out.println("Creating new version of object");
        object = new DigitalObject.Builder(object.getContent()).title("mytitle").build();
        System.out.println("asserting the title is not null");
        assertNotNull("NOT expecting object.getTitle() to be null", object.getTitle());
        if (storeFlag) {
            DigitalObject retObject = null;
            System.out.println("now retrieving object to test (ret object)");
            System.out.println("Retrieving the test object using URI:" + pdURI);
            try {
                retObject = DigitalObjectManagerTests.dom.retrieve(pdURI);
            } catch (DigitalObjectNotFoundException e) {
                e.printStackTrace();
                throw e;
            } catch (Exception e) {
                System.out.println("Caught unexpected exception calling retrieve()");
                e.printStackTrace();
                fail("this.dom.retrive failed with an unexpected exception");
            }
            System.out.println("Creating new Purl");
            URI newPurl = new File(AllStorageSuite.TEST_DATA_BASE, FILE).toURI();
            System.out.println("Creating digital object c2");
            DigitalObjectContent c2 = Content.byReference(newPurl.toURL().openStream());
            System.out.println("Creating new Expected object");
            DigitalObject expectedObject = new DigitalObject.Builder(c2).build();
            assertNotNull("Not expecting returned object to be null", retObject);
            System.out.println("Trying content match between expectedObject and retobject");
            if (retObject != null) {
                assertEquals("Retrieve Digital Object content (" + expectedObject.getContent() + ") doesn't match that stored (" + retObject.getContent() + ")", expectedObject.getContent(), retObject.getContent());
            }
            System.out.println("trying list out");
            List<URI> rootResults = DigitalObjectManagerTests.dom.list(null);
            System.out.println("getting expectedResults");
            List<URI> expectedResults = new ArrayList<URI>();
            System.out.println("adding entry to expectedResults");
            expectedResults.add(new URI("planets://localhost:8080/dr/test/" + FILE));
            System.out.println("testing count");
            assertEquals("Original and retrieved result count should be equal;", expectedResults.size(), rootResults.size());
            System.out.println("getting testResults from list");
            List<URI> testResults = DigitalObjectManagerTests.dom.list(rootResults.get(0));
            assertEquals("Original and retrieved result count should be equal;", expectedResults.size(), testResults.size());
            for (int iLoop = 0; iLoop < expectedResults.size(); iLoop++) {
            }
        }
    }
