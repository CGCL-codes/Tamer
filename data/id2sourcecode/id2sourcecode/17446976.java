    protected void genMethod(Class<?> msg, StringBuilder sb, ExternalizableFactory factory) {
        String c_name = msg.getCanonicalName();
        String s_name = msg.getSimpleName();
        String m_name = c_name.replace('.', '_');
        StringBuilder read = new StringBuilder();
        StringBuilder write = new StringBuilder();
        for (Field f : msg.getFields()) {
            int modifiers = f.getModifiers();
            if (!Modifier.isStatic(modifiers)) {
                genCodecField(msg, f, read, write, factory);
            }
        }
        sb.append("//	----------------------------------------------------------------------------------------------------\n");
        sb.append("//	" + c_name + "\n");
        sb.append("//	----------------------------------------------------------------------------------------------------\n");
        sb.append("	private void _r(" + c_name + " msg, NetDataInput in) throws IOException {\n");
        sb.append(read);
        sb.append("	}\n");
        sb.append("	private void _w(" + c_name + " msg, NetDataOutput out) throws IOException {\n");
        sb.append(write);
        sb.append("	}\n\n");
    }
