    protected void generateHelper(TypeName t, final TabPrintWriter writer) {
        writer.startLine("private " + t.name() + " read" + t.identifierName() + "()");
        writer.print(" throws java.io.IOException {");
        writer.indent();
        t.accept(new TypeNameVisitor<Void>() {

            public Void forTreeNode(ClassName t) {
                throw error(t);
            }

            public Void forPrimitive(PrimitiveName t) {
                throw error(t);
            }

            public Void forString(ClassName t) {
                throw error(t);
            }

            public Void forGeneralClass(ClassName t) {
                throw error(t);
            }

            private RuntimeException error(TypeName t) {
                return new IllegalArgumentException("Unexpected type for helper method: " + t.name());
            }

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

            public Void forReferenceArray(ReferenceArrayName t) {
                handleSequence(t);
                return null;
            }

            public Void forSequenceClass(SequenceClassName t) {
                handleSequence(t);
                return null;
            }

            private void handleSequence(SequenceName t) {
                Pair<String, Boolean> readElt = elementReadString(t.elementType(), false);
                writer.startLine("readExpected(\"{\");");
                writer.startLine(t.accumulator("accum"));
                writer.startLine("while (readMoreListElements()) {");
                writer.indent();
                if (readElt.second()) {
                    writer.startLine("@SuppressWarnings(\"unchecked\")");
                    writer.startLine(t.elementType().name() + " elt_result = " + readElt.first() + ";");
                    writer.startLine(t.addToAccumulator("accum", "elt_result"));
                } else {
                    writer.startLine(t.addToAccumulator("accum", readElt.first()));
                }
                writer.unindent();
                writer.startLine("}");
                writer.startLine("return " + t.constructor("accum") + ";");
            }

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

            public Void forTupleClass(TupleClassName t) {
                List<TypeName> elementTypes = t.elementTypes();
                List<String> elements = new ArrayList<String>(elementTypes.size());
                writer.startLine("c = readCharWord();");
                writer.startLine("if (c != '(') throw error();");
                for (int i = 0; i < elementTypes.size(); i++) {
                    if (i > 0) {
                        writer.startLine("c = readCharWord();");
                        writer.startLine("if (c != ',') throw error();");
                    }
                    TypeName eltT = elementTypes.get(i);
                    Pair<String, Boolean> readElt = elementReadString(eltT, false);
                    String resultName = "elt_result_" + i;
                    elements.add(resultName);
                    if (readElt.second()) {
                        writer.startLine("@SuppressWarnings(\"unchecked\")");
                    }
                    writer.startLine(eltT.name() + " " + resultName + " = " + readElt.first() + ";");
                }
                writer.startLine("c = readCharWord();");
                writer.startLine("if (c != ')') throw error();");
                writer.startLine("return " + t.constructor(elements) + ";");
                return null;
            }
        });
        writer.unindent();
        writer.startLine("}");
        writer.println();
    }
