    public void testNext() throws IOException, URISyntaxException {
        JobConf jobConf = new JobConf();
        Path out = new Path(_file.getAbsolutePath());
        FileOutputFormat.setOutputPath(jobConf, out);
        Mockery mockery = new Mockery();
        Path path = new Path("src/test/testIndexB/aIndex.zip");
        FileSystem fileSystem = FileSystem.get(jobConf);
        long len = fileSystem.getFileStatus(path).getLen();
        FileSplit fileSplit = new FileSplit(path, 0, len, (String[]) null);
        final IDocumentDuplicateInformation duplicateInformation = mockery.mock(IDocumentDuplicateInformation.class);
        DfsIndexRecordReader reader = new DfsIndexRecordReader(jobConf, fileSplit, duplicateInformation);
        mockery.checking(new Expectations() {

            {
                atLeast(1).of(duplicateInformation).getKeyField();
                will(returnValue("foo"));
                atLeast(1).of(duplicateInformation).getSortField();
                will(returnValue("foo"));
            }
        });
        Text text = reader.createKey();
        DocumentInformation information = reader.createValue();
        reader.next(text, information);
        assertEquals("bar", text.toString());
        assertEquals("bar", information.getSortValue().toString());
        assertEquals(0, information.getDocId().get());
        assertEquals(new File(out.toString(), ".indexes/" + path.getName() + "-" + MD5Hash.digest(path.toString()) + "-uncompress").getAbsolutePath(), new File(new URI(information.getIndexPath().toString())).getAbsolutePath());
        mockery.assertIsSatisfied();
    }
