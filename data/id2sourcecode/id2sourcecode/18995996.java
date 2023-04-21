    public static void main(final String[] args) throws IOException {
        final PrintStream out = new PrintStream("engine/CmdList.java");
        out.println();
        out.println("// Do not edit by hand. Use CmdInit.");
        out.println("package engine;");
        out.println("import java.util.Map;");
        out.println("import java.util.HashMap;");
        out.println("public class CmdList {");
        out.println("public final static Map<String, Command>	dict			= new HashMap<String, Command>();");
        out.println("public final static Map<String, String>	dictNames		= new HashMap<String, String>();");
        out.println("public final static Map<String, String>	dictNamesTurtle	= new HashMap<String, String>();");
        out.println("static {");
        for (final String name : dict) out.println("dict.put(\"" + cmdName(name) + "\", new " + name + "());");
        for (final String name : dictNames) out.println("dictNames.put(\"" + cmdName(name) + "\", \"" + name + "\");");
        for (final String name : dictNamesTurtle) out.println("dictNamesTurtle.put(\"" + cmdName(name) + "\", \"" + name + "\");");
        out.println("}}");
        final Process p = Runtime.getRuntime().exec("javac engine/CmdList.java");
        int read;
        while ((read = p.getErrorStream().read()) != -1) System.out.write(read);
    }
