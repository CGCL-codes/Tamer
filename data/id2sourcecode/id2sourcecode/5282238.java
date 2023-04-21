    public static void runMain(String[] argv) throws IOException {
        FileOutputStream fos = new FileOutputStream(output_jar_name);
        JarOutputStream out_jar_stream = new JarOutputStream(fos);
        for (int argno = 0; argno < argv.length; argno++) {
            if (argv[argno].endsWith(".jar")) {
                instrument_jar_file(argv[argno], out_jar_stream);
            } else {
                String class_name = argv[argno];
                if (!class_name.endsWith(".class")) class_name = class_name + ".class";
                JarEntry je = new JarEntry(class_name.toString());
                je.setMethod(ZipEntry.DEFLATED);
                out_jar_stream.putNextEntry(je);
                String instr_class_file = instrument_class(argv[argno], new ClassParser(argv[argno]));
                FileInputStream src = new FileInputStream(instr_class_file);
                copyStreamToJarStream(out_jar_stream, src);
                src.close();
                if (!((new File(instr_class_file)).delete())) System.out.println("Warning: unable to delete file: " + instr_class_file);
            }
        }
        if (out_jar_stream != null) out_jar_stream.close();
        System.out.println("Finishing pack bytecode!");
    }
