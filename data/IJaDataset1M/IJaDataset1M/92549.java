package edu.rice.cs.astgen;

import edu.rice.cs.plt.iter.IterUtil;
import edu.rice.cs.plt.tuple.Option;

/** Shared code for visitor code generators. */
public abstract class VisitorGenerator extends CodeGenerator {

    public VisitorGenerator(ASTModel ast) {
        super(ast);
    }

    public void generateAdditionalCode() {
        for (NodeType root : ast.rootTypes()) {
            generateVisitor(root);
        }
    }

    /** Generate a visitor for the given root type. */
    protected abstract void generateVisitor(NodeType root);

    protected String visitorMethodName(NodeType c) {
        return options.visitorMethodPrefix + upperCaseFirst(c.name());
    }

    protected void outputVisitorInterfaceCases(NodeType root, TabPrintWriter writer, String retType) {
        for (NodeType t : ast.descendents(root)) {
            if (!t.isAbstract()) {
                writer.println();
                writer.startLine("/** Process an instance of " + t.name() + ". */");
                writer.startLine("public " + retType + " " + visitorMethodName(t) + "(" + t.name() + " that);");
            }
        }
    }

    protected void outputAbstractVisitorCases(NodeType root, TabPrintWriter writer, String retType) {
        for (NodeType t : ast.descendents(root)) {
            if (!t.isAbstract()) {
                writer.println();
                writer.startLine("/** Process an instance of " + t.name() + ". */");
                writer.startLine("public abstract " + retType + " " + visitorMethodName(t) + "(" + t.name() + " that);");
            }
        }
    }

    protected void outputDefaultCaseMethod(TabPrintWriter writer, NodeType root) {
        writer.startLine("/**");
        writer.startLine(" * This method is run for all cases that are not handled elsewhere.");
        writer.startLine(" * By default, an exception is thrown; subclasses may override this behavior.");
        writer.startLine(" * @throws IllegalArgumentException");
        writer.startLine("**/");
        writer.startLine("public RetType defaultCase(" + root.name() + " that) {");
        writer.indent();
        writer.startLine("throw new IllegalArgumentException(\"Visitor \" + getClass().getName()");
        writer.print(" + \" does not support visiting values of type \" + that.getClass().getName());");
        writer.unindent();
        writer.startLine("}");
    }

    protected void outputDefaultCaseVoidMethod(TabPrintWriter writer, NodeType root) {
        writer.startLine("/**");
        writer.startLine(" * This method is run for all cases that are not handled elsewhere.");
        writer.startLine(" * By default, it is a no-op; subclasses may override this behavior.");
        writer.startLine("**/");
        writer.startLine("public void defaultCase(" + root.name() + " that) {");
        writer.print("}");
    }

    protected void outputDelegatingForCase(NodeType t, TabPrintWriter writer, NodeType root, String retType, String suff, String defaultMethod) {
        outputForCaseHeader(t, writer, retType, suff);
        writer.indent();
        if (!retType.equals("void")) {
            writer.startLine("return ");
        } else {
            writer.startLine();
        }
        Option<NodeType> sup = ast.parent(t);
        if (sup.isSome() && !t.equals(root)) {
            writer.print(visitorMethodName(sup.unwrap()) + suff + "(that);");
        } else {
            writer.print(defaultMethod + "(that);");
        }
        writer.unindent();
        writer.startLine("}");
        writer.println();
    }

    protected void outputForCaseHeader(NodeType t, TabPrintWriter writer, String retType, String suff) {
        outputForCaseHeader(t, writer, retType, suff, IterUtil.<String>empty());
    }

    protected void outputForCaseHeader(NodeType t, TabPrintWriter writer, String retType, String suff, Iterable<String> extraParams) {
        writer.startLine("public ");
        writer.print(retType);
        writer.print(" ");
        writer.print(visitorMethodName(t));
        writer.print(suff + "(");
        writer.print(t.name() + " that");
        for (String p : extraParams) {
            writer.print(", " + p);
        }
        writer.print(") {");
    }
}
