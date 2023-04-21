    public static boolean serve(String name, String mountLocation, java.io.FileDescriptor fileDescriptor) {
        if (formatterClass == null) {
            return false;
        }
        Method createMethod = null;
        try {
            createMethod = formatterClass.getDeclaredMethod("create", String.class, FileChannel.class, String.class);
        } catch (Exception any) {
            any.printStackTrace();
            return false;
        }
        try {
            createMethod.invoke(null, name, new FileInputStream(fileDescriptor).getChannel(), mountLocation);
        } catch (Exception any) {
            any.printStackTrace();
        }
        return true;
    }
