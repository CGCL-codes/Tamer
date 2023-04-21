package org.apache.tika.parser.rtf;

class GroupState {

    public int depth;

    public boolean bold;

    public boolean italic;

    public boolean ignore;

    public int ucSkip = 1;

    public String fontCharset;

    public GroupState() {
    }

    public GroupState(GroupState other) {
        bold = other.bold;
        italic = other.italic;
        ignore = other.ignore;
        ucSkip = other.ucSkip;
        fontCharset = other.fontCharset;
        depth = 1 + other.depth;
    }
}
