    private void testStoreAsNew(URI uri, DigitalObjectManager dom) {
        if (!dom.isWritable(null)) return;
        for (TestFile file : DataRegistryTests.testFiles) {
            try {
                File testFile = new File(file.getLocation());
                URI purl = testFile.toURI();
                String name = testFile.getName();
                System.out.println("PURL is " + file.getLocation());
                DigitalObjectContent content = Content.byReference(purl.toURL().openStream());
                System.out.println("created content " + content);
                DigitalObject object = new DigitalObject.Builder(content).permanentUri(purl).title(purl.toString()).build();
                System.out.println("created object " + object);
                URI theLoc = null;
                if (uri != null) theLoc = DataRegistryTests.dataReg.storeAsNew(new URI(uri.toString() + "/" + name), object); else theLoc = DataRegistryTests.dataReg.storeAsNew(object);
                System.out.println("got theLoc = " + theLoc);
                DigitalObjectContent expectCont = Content.byReference(purl.toURL().openStream());
                DigitalObject expectObj = new DigitalObject.Builder(expectCont).build();
                DigitalObject retObject = dom.retrieve(theLoc);
                assertEquals("Retrieve Digital Object content doesn't match that stored", expectObj.getContent(), retObject.getContent());
            } catch (MalformedURLException e) {
                e.printStackTrace();
                fail("Couldn't get URl from URI ");
            } catch (IOException e) {
                e.printStackTrace();
                fail("IOException accessing file");
            } catch (DigitalObjectNotStoredException e) {
                e.printStackTrace();
                fail("Couldn't store digital object");
            } catch (DigitalObjectNotFoundException e) {
                e.printStackTrace();
                fail("Couldn't retrieve stored object");
            } catch (URISyntaxException e) {
                e.printStackTrace();
                fail("Couldn't create URI for" + uri.toString() + " file " + file);
            }
        }
    }
