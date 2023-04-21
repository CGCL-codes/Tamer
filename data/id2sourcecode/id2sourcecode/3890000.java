    public void test(String rwLang, int seed, int jjjMax, String[] wopName, Object[] wopVal) throws IOException {
        Model m1 = createMemModel();
        Model m2 = createMemModel();
        test = "testWriterAndReader lang=" + rwLang + " seed=" + seed;
        String filebase = "testing/regression/testWriterAndReader/";
        if (showProgress) System.out.println("Beginning " + test);
        Random random = new Random(seed);
        RDFReader rdfRdr = m1.getReader(rwLang);
        RDFWriter rdfWtr = m1.getWriter(rwLang);
        if (wopName != null) {
            for (int i = 0; i < wopName.length; i++) {
                rdfWtr.setProperty(wopName[i], wopVal[i]);
            }
        }
        rdfRdr.setErrorHandler(this);
        rdfWtr.setErrorHandler(this);
        for (int jjj = 0; jjj < jjjMax; jjj++) {
            String fileName = "t" + (fileNumber * 1000) + ".rdf";
            m1 = createMemModel();
            String baseUriRead;
            if (fileNumber < baseUris.length) baseUriRead = baseUris[fileNumber]; else baseUriRead = "http://foo.com/Hello";
            InputStream rdr = new FileInputStream(filebase + fileName);
            m1.read(rdr, baseUriRead);
            rdr.close();
            for (int j = 0; j < repetitionsJ; j++) {
                String baseUriWrite = j % 2 == 0 ? baseUriRead : "http://bar.com/irrelevant";
                int cn = (int) m1.size();
                if ((j % 2) == 0 && j > 0) prune(m1, random, 1 + cn / 10);
                if ((j % 2) == 0 && j > 0) expand(m1, random, 1 + cn / 10);
                tmpOut = new ByteArrayOutputStream();
                rdfWtr.write(m1, tmpOut, baseUriWrite);
                tmpOut.flush();
                tmpOut.close();
                m2 = createMemModel();
                InputStream in = new ByteArrayInputStream(tmpOut.toByteArray());
                rdfRdr.read(m2, in, baseUriWrite);
                in.close();
                Model s1 = m1;
                Model s2 = m2;
                assertTrue("Comparison of file written out, and file read in.", s1.isIsomorphicWith(s2));
                tmpOut.reset();
                tmpOut = null;
            }
            if (showProgress) {
                System.out.print("+");
                System.out.flush();
            }
        }
        if (showProgress) System.out.println("End of " + test);
    }
