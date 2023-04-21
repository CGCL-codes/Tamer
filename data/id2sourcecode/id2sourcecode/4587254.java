    @Test
    public void testLock() throws CoreException, IOException {
        IFolder folder = project.getFolder(new Path("src"));
        IFile file = folder.getFile("test.simplemodel");
        IFile baseFile = folder.getFile(new Path("TestBase.java"));
        file.create(new ByteArrayInputStream(new byte[0]), true, null);
        file.setContents(new ByteArrayInputStream("package Test{options{ lock readwrite;}}".getBytes()), true, true, null);
        ResourcesPlugin.getWorkspace().build(IncrementalProjectBuilder.FULL_BUILD, null);
        assertTrue(baseFile.exists());
        assertEquals(TestUtil.getAstString(new StringBuilder("import java.util.concurrent.locks.ReadWriteLock;").append("import java.util.concurrent.locks.").append("ReentrantReadWriteLock;").append("public class TestBase{").append("public final ReadWriteLock LOCK = ").append("new ReentrantReadWriteLock();}").toString()), TestUtil.getAstString(baseFile));
        baseFile.delete(true, null);
        file.setContents(new ByteArrayInputStream("package Test{options{ lock reentrant;}}".getBytes()), true, true, null);
        ResourcesPlugin.getWorkspace().build(IncrementalProjectBuilder.FULL_BUILD, null);
        assertTrue(baseFile.exists());
        assertEquals(TestUtil.getAstString(new StringBuilder("import java.util.concurrent.locks.Lock;").append("import java.util.concurrent.locks.ReentrantLock;").append("public class TestBase{").append("public final Lock LOCK = new ReentrantLock();}").toString()), TestUtil.getAstString(baseFile));
        baseFile.delete(true, null);
        file.setContents(new ByteArrayInputStream("package Test{options{ lock synchronized;}}".getBytes()), true, true, null);
        ResourcesPlugin.getWorkspace().build(IncrementalProjectBuilder.FULL_BUILD, null);
        assertTrue(baseFile.exists());
        assertEquals(TestUtil.getAstString("public class TestBase{}"), TestUtil.getAstString(baseFile));
        baseFile.delete(true, null);
        file.setContents(new ByteArrayInputStream("package Test{options{ lock none;}}".getBytes()), true, true, null);
        ResourcesPlugin.getWorkspace().build(IncrementalProjectBuilder.FULL_BUILD, null);
        assertTrue(baseFile.exists());
        assertEquals(TestUtil.getAstString("public class TestBase{}"), TestUtil.getAstString(baseFile));
    }
