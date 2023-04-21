    public static void testTree(int nodeCount, int blockWidth, boolean randomWidths) throws Exception {
        int version = _rand.nextInt(1000);
        ContentName theName = ContentName.fromNative(baseName, "testDocBuffer.txt");
        theName = VersioningProfile.addVersion(theName, version);
        try {
            ContentObject[] cos = makeContent(theName, nodeCount, blockWidth, randomWidths);
            CCNMerkleTree tree = new CCNMerkleTree(cos, pair.getPrivate());
            tree.setSignatures();
            System.out.println("Constructed tree of numleaves: " + tree.numLeaves() + " max pathlength: " + tree.maxDepth());
            ContentObject block;
            for (int i = 0; i < tree.numLeaves() - 1; ++i) {
                block = cos[i];
                boolean result = block.verify(pair.getPublic());
                if (!result) {
                    System.out.println("Block name: " + tree.segmentName(i) + " num " + i + " verified? " + result + ", content: " + DataUtils.printBytes(block.digest()));
                    byte[] digest = CCNDigestHelper.digest(block.encode());
                    byte[] tbsdigest = CCNDigestHelper.digest(ContentObject.prepareContent(block.name(), block.signedInfo(), block.content()));
                    System.out.println("Raw content digest: " + DataUtils.printBytes(CCNDigestHelper.digest(block.content())) + " object content digest:  " + DataUtils.printBytes(CCNDigestHelper.digest(block.content())));
                    System.out.println("Block: " + block.name() + " timestamp: " + block.signedInfo().getTimestamp() + " encoded digest: " + DataUtils.printBytes(digest) + " tbs content: " + DataUtils.printBytes(tbsdigest));
                } else if (i % 100 == 0) {
                    System.out.println("Block name: " + tree.segmentName(i) + " num " + i + " verified? " + result + ", content: " + DataUtils.printBytes(block.digest()));
                }
                Assert.assertTrue("Path " + i + " failed to verify.", result);
            }
            tree = null;
        } catch (Exception e) {
            System.out.println("Exception in testTree: " + e.getClass().getName() + ": " + e.getMessage());
            e.printStackTrace();
            throw (e);
        }
    }
