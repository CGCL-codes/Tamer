    private HeapHeader getHeapHeader(final HeapHeader heapHeaderReference) throws HeapException {
        HeapHeader heapHeader = heapElementManager.getHeapHeader();
        assertEquals("header must be at file beginning", 0, heapHeader.getPositionInFile());
        if (heapHeaderReference != null) {
            assertNotSame("new header must not be same of creation one", heapHeaderReference, heapHeader);
            assertEquals("writed and readed must be equals", heapHeaderReference, heapHeader);
        }
        return heapHeader;
    }
