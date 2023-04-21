    private void appendHeaderBody(SourceCodeWriter writer) {
        for (ObjcType subType : subTypes) {
            subType.append(writer);
        }
        if (comments != null) {
            writer.appendDocComment(comments);
        }
        if (!isProtocol) {
            boolean statics = false;
            for (ObjcField field : fields) {
                if (ModifierSet.isStatic(field.getModifiers()) && ModifierSet.isPublic(field.getModifiers())) {
                    if (!statics) {
                        statics = true;
                        writer.newLine();
                    }
                    writer.append(field);
                }
            }
        }
        if (isProtocol) {
            writer.newLine().append("@protocol ").append(name);
        } else {
            writer.newLine().append("@interface ").append(name).append(" : ").append(baseClass.getName());
        }
        if (protocols.size() > 0) {
            writer.append(" <");
            boolean first = true;
            for (ObjcType protocol : protocols) {
                if (first) {
                    first = false;
                } else {
                    writer.append(", ");
                }
                writer.append(protocol.getName());
            }
            writer.append('>');
        }
        if (!isProtocol) {
            writer.append(" {").indent().newLine();
            for (ObjcField field : fields) {
                if (!ModifierSet.isStatic(field.getModifiers())) {
                    writer.append(field);
                }
            }
            writer.unIndent();
            writer.append("}").newLine();
        }
        writer.newLine();
        for (ObjcMethod m : methods) {
            if (m.isGetter()) {
                writer.append("@property(nonatomic");
                if (m.getReturnType().isPointerType()) {
                    writer.append(", retain");
                }
                if (!hasPropertySetter(m.getPropertyName())) {
                    writer.append(", readonly");
                }
                writer.append(") ").append(m.getReturnType().getPointerTypeName()).append(' ').append(m.getPropertyName()).endStatement();
            }
        }
        for (ObjcMethod m : methods) {
            if (!m.isGetter() && !ModifierSet.isPrivate(m.getModifiers())) {
                writer.append(m);
            }
        }
        writer.append("@end").newLine();
    }
