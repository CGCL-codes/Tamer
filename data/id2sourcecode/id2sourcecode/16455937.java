            public Void forPrimitiveArray(PrimitiveArrayName t) {
                String boxedName = t.elementType().boxed().name();
                String readElt = elementReadString(t.elementType(), false).first();
                String accumType = "java.util.ArrayList<" + boxedName + ">";
                writer.startLine(accumType + " accum = new " + accumType + "();");
                writer.startLine("java.lang.String word = readWord();");
                writer.startLine("if (!word.equals(\"{\")) throw error();");
                writer.startLine("word = readWord();");
                writer.startLine("while (word.equals(\"*\")) {");
                writer.indent();
                writer.startLine("accum.add(" + readElt + ");");
                writer.startLine("word = readWord();");
                writer.unindent();
                writer.startLine("}");
                writer.startLine("if (!word.equals(\"}\")) throw error();");
                writer.startLine(t.name() + " result = new " + t.elementType().name() + "[accum.size()];");
                writer.startLine("int i = 0;");
                writer.startLine("for (" + boxedName + " elt : accum) result[i++] = elt;");
                writer.startLine("return result;");
                return null;
            }
