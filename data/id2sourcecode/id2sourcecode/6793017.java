    String write(ITerm t, Set<String> alreadyWritten) {
        StringBuilder out = new StringBuilder();
        if (t == null) {
            throw new RuntimeException();
        }
        out.append(t.getName() + "\t:" + t.getType());
        if (!alreadyWritten.contains(t.getName())) {
            alreadyWritten.add(t.getName());
            switch(t.getType()) {
                case and:
                case or:
                    {
                        ITermCompound c = (ITermCompound) t;
                        out.append("(");
                        for (ITerm s : c.getSubs()) {
                            out.append(write(s, alreadyWritten));
                            out.append(",");
                        }
                        out.append(")");
                        break;
                    }
                case reference:
                    {
                        ITermReference m = (ITermReference) t;
                        out.append("(");
                        if (m.getSub() == null) {
                            throw new RuntimeException("error in language: no child: " + t.getName() + "\t:" + t.getType());
                        }
                        out.append(write(m.getSub(), alreadyWritten));
                        out.append(")");
                        break;
                    }
                case oneormore:
                case zeroormore:
                    {
                        ITermMore m = (ITermMore) t;
                        out.append("(");
                        out.append(write(m.getSub(), alreadyWritten));
                        out.append(")");
                        break;
                    }
            }
        }
        return out.toString();
    }
