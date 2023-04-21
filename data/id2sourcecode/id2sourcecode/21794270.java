    private void runTestTarOutputStream(File f) throws IOException {
        TarInputStream origIn = new TarInputStream(new FileInputStream(f));
        File testFile = File.createTempFile("jaxlib.arc.tar.TarOutputStreamTest", ".tar");
        testFile.deleteOnExit();
        int fileCount = 0;
        TarOutputStream tarOut = new TarOutputStream(new BufferedXOutputStream(new FileOutputStream(testFile)));
        try {
            for (TarEntry entry; (entry = origIn.openEntry()) != null; ) {
                entry.verify();
                tarOut.openEntry(entry);
                if (entry.getType() == TarEntryType.NORMAL) {
                    fileCount++;
                    long remaining = entry.getSize();
                    while (remaining > 0) remaining -= tarOut.transferFrom(origIn, remaining);
                }
                tarOut.closeEntry();
                origIn.closeEntry();
            }
        } finally {
            origIn.close();
            tarOut.close();
        }
        assertEquals(TarTestUtils.getCountFilesInTestArchive(), fileCount);
        origIn = new TarInputStream(new FileInputStream(TarTestUtils.getTestArchiveFile()));
        TarInputStream tarIn = new TarInputStream(new BufferedXInputStream(new FileInputStream(testFile)));
        try {
            for (TarEntry a; (a = origIn.openEntry()) != null; ) {
                TarEntry b = tarIn.openEntry();
                if (!a.getPath().equals(b.getPath())) fail("expected\n'" + a.getPath() + "'\n but got \n'" + b.getPath() + "'");
                assertEquals(a.getPermissions(), b.getPermissions());
                assertEquals(a.getUserId(), b.getUserId());
                assertEquals(a.getGroupId(), b.getGroupId());
                assertEquals(a.getSize(), b.getSize());
                assertEquals(a.getTimeLastModified(), b.getTimeLastModified());
                assertEquals(a.getType(), b.getType());
                assertEquals(a.getLinkedPath(), b.getLinkedPath());
                assertEquals(a.getUserName(), b.getUserName());
                assertEquals(a.getGroupName(), b.getGroupName());
                assertEquals(a.getDeviceMajor(), b.getDeviceMajor());
                assertEquals(a.getDeviceMinor(), b.getDeviceMinor());
                for (int byteA; (byteA = origIn.read()) >= 0; ) {
                    assertEquals(byteA, tarIn.read());
                }
                origIn.closeEntry();
                tarIn.closeEntry();
            }
        } finally {
            origIn.close();
            tarIn.close();
        }
        if (TarTestUtils.testArchiveUsingTar(testFile) == 0) Logger.global.warning("readed == 0");
    }
