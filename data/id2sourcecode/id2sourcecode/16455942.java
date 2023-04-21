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
