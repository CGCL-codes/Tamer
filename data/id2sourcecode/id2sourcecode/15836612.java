    public void includeStandardSourceHeaders(Writer output) throws IOException {
        Util.generateComment(output, "Standard Jace headers needed to implement this class.");
        output.write("#include \"jace/JArguments.h\"" + newLine);
        output.write("#include \"jace/JMethod.h\"" + newLine);
        output.write("#include \"jace/JField.h\"" + newLine);
        output.write("#include \"jace/JClassImpl.h\"" + newLine);
        String className = classFile.getClassName().asIdentifier();
        if (className.equals("java.lang.String")) output.write("#include \"jace/proxy/java/lang/Integer.h\"" + newLine);
        output.write("#include \"jace/BoostWarningOff.h\"" + newLine);
        output.write("#include <boost/thread/mutex.hpp>" + newLine);
        output.write("#include \"jace/BoostWarningOn.h\"" + newLine);
    }
