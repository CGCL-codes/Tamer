    @Test
    public void tFailExceptions() throws BasicIOException {
        FileSystem fs = new FileSystem("C:\\WINDOWS");
        fs = new FileSystem(new java.io.File("C:\\WINDOWS"));
        try {
            fs = new FileSystem((String) null);
            fail();
        } catch (IllegalArgumentException iae) {
            println(iae);
        }
        try {
            fs = new FileSystem((java.io.File) null);
            fail();
        } catch (IllegalArgumentException iae) {
            println(iae);
        }
        try {
            fs = new FileSystem("C:\\", null, "password");
            fail();
        } catch (IllegalArgumentException iae) {
            println(iae);
        }
        try {
            fs = new FileSystem("C:\\", "username", null);
            fail();
        } catch (IllegalArgumentException iae) {
            println(iae);
        }
        try {
            fs = new FileSystem("C:\\WINDOWS?", "username", null);
            fail();
        } catch (IllegalArgumentException iae) {
            println(iae);
        }
        try {
            fs = new FileSystem("C:\\AopD823khlw34S\\J8274KKL");
            fail();
        } catch (FileNotFoundException dnfe) {
            println(dnfe);
        }
        try {
            fs = new FileSystem("C:\\WINDOWS\\explorer.exe");
            fail();
        } catch (FileNotFoundException dnfe) {
            println(dnfe);
        }
        fs = new FileSystem(_testLocation);
        Directory dir = fs.getDirectory("test-exceptions");
        if (dir != null) {
            try {
                dir.deleteContents();
            } catch (OperationFailedException ofe) {
                println("WARNING: could not delete file " + ofe.getMessage());
            }
        } else {
            dir = fs.mkdirs("test-exceptions");
        }
        File readOnly = dir.createFile("read-only-file");
        ExtendedWriter writer = new ExtendedWriter(readOnly);
        writer.write("this is one line on the file\r\n");
        writer.close();
        readOnly.setWritable(false);
        assertFalse(readOnly.getWritable());
        try {
            writer = new ExtendedWriter(readOnly);
            fail();
        } catch (WriteAccessDeniedException wade) {
            println(wade);
        }
        try {
            readOnly.getFileSystem().setIgnoreReadOnly(false);
            readOnly.delete();
            fail();
        } catch (WriteAccessDeniedException wade) {
            println(wade);
        }
        try {
            dir.delete();
            fail();
        } catch (OperationFailedException ofe) {
            println(ofe);
        }
        try {
            dir.createFile("read-only-file");
            fail();
        } catch (FileAlreadyExistsException fae) {
            println(fae);
        }
        readOnly.getFileSystem().setIgnoreReadOnly(true);
        readOnly.delete();
        try {
            readOnly.getLastModified();
            fail();
        } catch (FileNotFoundException fnfe) {
            println(fnfe);
        }
        String filename = "shopping-list.csv";
        File other = dir.createFile(filename);
        ExtendedOutputStream stream = other.getOutputStreamAndLock();
        try {
            other.getInputStream();
            fail();
        } catch (FileLockedException fle) {
            println(fle);
        }
        try {
            stream = other.getOutputStream();
            fail();
        } catch (FileLockedException fle) {
            println(fle);
        }
        try {
            stream = other.getOutputStreamAndLock();
            fail();
        } catch (FileLockedException fle) {
            println(fle);
        }
        try {
            other.delete();
            fail();
        } catch (OperationFailedException ofe) {
            println(ofe);
        }
        if (_testExcelLock) {
            println("Please attempt to open file '" + other.getAbsolutePath() + "' with Excel.");
            println("This should result in an error message in Excel.");
            println("When finished, please press ENTER:");
            Tools.readInput();
        }
        stream.close();
        if (_testExcelLock) {
            println("Please open file '" + other.getAbsolutePath() + "' with Excel.");
            println("This will cause the unit test to get a file lock exception.");
            println("Please press ENTER when the file is open:");
            Tools.readInput();
            try {
                stream = other.getOutputStreamAndLock();
                fail();
            } catch (FileLockedException fle) {
                println(fle);
            }
            stream.close();
            println("Now please close Excel and press ENTER:");
            Tools.readInput();
        }
        stream = other.getOutputStreamAndLock();
        stream.close();
        try {
            stream.flush();
            fail();
        } catch (IllegalStateException ise) {
            println(ise);
        }
        stream = other.getOutputStream();
        ExtendedOutputStream anotherStream = other.getOutputStream(true);
        stream.write("abc".getBytes());
        anotherStream.write("def".getBytes());
        stream.close();
        anotherStream.close();
        ExtendedReader reader = new ExtendedReader(other.getInputStream());
        assertEquals(reader.readLine(), "abcdef");
        reader.close();
        ExtendedInputStream in = other.getInputStream();
        try {
            in.reset();
            fail();
        } catch (IllegalStateException ise) {
            println(ise);
        }
        in.mark(1000);
        char[] array = new char[] { 'a', 'b', 'c', 'd', 'e', 'f' };
        for (int i = 0; i < array.length; i++) {
            char c = (char) in.read();
            assertEquals(c, array[i]);
        }
        in.reset();
        for (int i = 0; i < array.length; i++) {
            char c = (char) in.read();
            assertEquals(c, array[i]);
        }
        in.close();
        try {
            in.mark(1000);
            fail();
        } catch (IllegalStateException ise) {
            println(ise);
        }
        if (_testJVMLock) {
            in = other.getInputStream();
            println("On another window, please run the LockStream class.");
            println("Please press ENTER when the program is running:");
            Tools.readInput();
            in.available();
            in.mark(1000);
            in.reset();
            try {
                in.skip(10);
                fail();
            } catch (FileLockedException fle) {
                println(fle);
            }
            try {
                in.read();
                fail();
            } catch (FileLockedException fle) {
                println(fle);
            }
            in.close();
            in = other.getInputStream();
            assertEquals(in.skip(1000), 1000);
            try {
                in.read();
                fail();
            } catch (FileLockedException fle) {
                println(fle);
            }
            reader = new ExtendedReader(in);
            assertFalse(reader.ready());
            try {
                reader.readLine();
                fail();
            } catch (FileLockedException fle) {
                println(fle);
            }
            reader.close();
            println("Please press ENTER when the program finishes:");
            Tools.readInput();
        }
        other.delete();
        String bigPath = "A876876BBN98273984738PPP454HHSDJK998";
        Directory bigDir = null;
        bigDir = dir.mkdirs(bigPath);
        for (int i = 0; i < 10; i++) {
            bigDir = bigDir.mkdirs(bigPath);
        }
        bigDir.listAll();
        bigDir.createFile("A_FILE.tmp");
        dir.delete(true);
    }
