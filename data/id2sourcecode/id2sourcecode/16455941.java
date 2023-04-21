            public Void forOptionClass(OptionClassName t) {
                Pair<String, Boolean> readElt = elementReadString(t.elementType(), false);
                writer.startLine("if (readOptionHeader()) {");
                writer.indent();
                if (readElt.second()) {
                    writer.startLine("@SuppressWarnings(\"unchecked\")");
                }
                writer.startLine(t.elementType().name() + " elt_result = " + readElt.first() + ";");
                writer.startLine("readExpected(\")\");");
                writer.startLine("return " + t.nonEmptyConstructor("elt_result") + ";");
                writer.unindent();
                writer.startLine("}");
                writer.startLine("else { return " + t.emptyConstructor() + "; }");
                return null;
            }
