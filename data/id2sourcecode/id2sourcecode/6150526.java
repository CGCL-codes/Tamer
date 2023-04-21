    public static void write(Molecule molecule, File file) throws FileFormatException, IOException {
        if (molecule == null) throw new NullPointerException("" + "The molecule parameter has null value.");
        if (file.exists() && !file.isFile()) throw new IOException("" + "The file " + file + " exists already but is not a file, overwrite how?");
        FileFormat format = getFormat(file);
        Exception exception = null;
        Class writer = null;
        Method write = null;
        try {
            writer = Class.forName("com.csol.chem.io." + format.toString() + "Writer");
            Class[] argTypes = { com.csol.chem.core.Molecule.class, java.io.File.class };
            write = writer.getDeclaredMethod("write", argTypes);
            Object[] args = new Object[2];
            args[0] = molecule;
            args[1] = file;
            write.invoke(null, args);
            if (false) throw new IOException("dummy, just so that I can catch " + "an IOException below if something has gone wrong with the " + "actual writing of the file");
        } catch (ClassNotFoundException e) {
            exception = e;
            System.err.println("Exception intercepted: " + e + S.lb + "when trying to write the file: \"" + file + "\"" + S.lb + "using class: " + writer + S.lb + " and method: " + write);
            e.printStackTrace();
            System.err.println("This probably has to do with not finding the class");
        } catch (SecurityException e) {
            exception = e;
            System.err.println("Exception intercepted: " + e + S.lb + "when trying to write the file: \"" + file + "\"" + S.lb + "using class: " + writer + S.lb + " and method: " + write);
            e.printStackTrace();
            System.err.println("This probably has to do with not finding the right method using the arg types");
        } catch (NoSuchMethodException e) {
            exception = e;
            System.err.println("Exception intercepted: " + e + S.lb + "when trying to write the file: \"" + file + "\"" + S.lb + "using class: " + writer + S.lb + " and method: " + write);
            e.printStackTrace();
            System.err.println("This probably has to do with not finding the right method using the arg types");
        } catch (IllegalArgumentException e) {
            exception = e;
            System.err.println("Exception intercepted: " + e + S.lb + "when trying to write the file: \"" + file + "\"" + S.lb + "using class: " + writer + S.lb + " and method: " + write);
            e.printStackTrace();
            System.err.println("This probably has to do with problems invoking the method");
        } catch (IllegalAccessException e) {
            exception = e;
            System.err.println("Exception intercepted: " + e + S.lb + "when trying to write the file: \"" + file + "\"" + S.lb + "using class: " + writer + S.lb + " and method: " + write);
            e.printStackTrace();
            System.err.println("This probably has to do with problems invoking the method");
        } catch (InvocationTargetException e) {
            exception = e;
            System.err.println("Exception intercepted: " + e + S.lb + "when trying to write the file: \"" + file + "\"" + S.lb + "using class: " + writer + S.lb + " and method: " + write);
            e.printStackTrace();
            System.err.println("This probably has to do with problems invoking the method");
        } catch (IOException e) {
            System.err.println("Exception intercepted: " + e + S.lb + "when trying to write the file: \"" + file + "\"" + S.lb + "using class: " + writer + S.lb + " and method: " + write);
            e.printStackTrace();
            System.err.println("This probably has to do with problems reading the actual molecule file rather than reflection");
            throw e;
        }
        if (exception != null) {
            Error error = new Error("Reflection invocation exception " + "when trying to write to file. \n" + "Original exception is: " + exception.getClass().toString() + "\n" + exception.toString());
            error.setStackTrace(exception.getStackTrace());
            throw error;
        }
    }
