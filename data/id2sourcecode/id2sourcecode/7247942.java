    public boolean lock() {
        String useNioProperty = System.getProperty("cobertura.use.java.nio");
        if (System.getProperty("java.version").startsWith("1.3") || ((useNioProperty != null) && useNioProperty.equalsIgnoreCase("false"))) {
            return true;
        }
        try {
            Class aClass = Class.forName("java.io.RandomAccessFile");
            Method method = aClass.getDeclaredMethod("getChannel", (Class[]) null);
            lockChannel = method.invoke(new RandomAccessFile(lockFile, "rw"), (Object[]) null);
        } catch (FileNotFoundException e) {
            System.err.println("Unable to get lock channel for " + lockFile.getAbsolutePath() + ": " + e.getLocalizedMessage());
            return false;
        } catch (InvocationTargetException e) {
            System.err.println("Unable to get lock channel for " + lockFile.getAbsolutePath() + ": " + e.getLocalizedMessage());
            return false;
        } catch (Throwable t) {
            System.err.println("Unable to execute RandomAccessFile.getChannel() using reflection: " + t.getLocalizedMessage());
            t.printStackTrace();
        }
        try {
            Class aClass = Class.forName("java.nio.channels.FileChannel");
            Method method = aClass.getDeclaredMethod("lock", (Class[]) null);
            lock = method.invoke(lockChannel, (Object[]) null);
        } catch (InvocationTargetException e) {
            System.err.println("---------------------------------------");
            e.printStackTrace(System.err);
            System.err.println("---------------------------------------");
            System.err.println("Unable to get lock on " + lockFile.getAbsolutePath() + ": " + e.getLocalizedMessage());
            System.err.println("This is known to happen on Linux kernel 2.6.20.");
            System.err.println("Make sure cobertura.jar is in the root classpath of the jvm ");
            System.err.println("process running the instrumented code.  If the instrumented code ");
            System.err.println("is running in a web server, this means cobertura.jar should be in ");
            System.err.println("the web server's lib directory.");
            System.err.println("Don't put multiple copies of cobertura.jar in different WEB-INF/lib directories.");
            System.err.println("Only one classloader should load cobertura.  It should be the root classloader.");
            System.err.println("---------------------------------------");
            return false;
        } catch (Throwable t) {
            System.err.println("Unable to execute FileChannel.lock() using reflection: " + t.getLocalizedMessage());
            t.printStackTrace();
        }
        return true;
    }
