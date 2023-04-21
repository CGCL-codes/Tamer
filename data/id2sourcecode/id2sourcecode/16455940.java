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
