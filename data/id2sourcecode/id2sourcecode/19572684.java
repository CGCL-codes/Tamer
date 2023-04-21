    protected void genCodecMethod(ExternalizableFactory factory, Class<?> msg, int msg_type, StringBuilder sb) {
        String c_name = msg.getCanonicalName();
        String s_name = msg.getSimpleName();
        String m_name = s_name + "_" + msg_type;
        StringBuilder read = new StringBuilder();
        StringBuilder write = new StringBuilder();
        for (Field f : msg.getFields()) {
            int modifiers = f.getModifiers();
            if (!Modifier.isStatic(modifiers)) {
                genCodecField(factory, msg, f, read, write);
            }
        }
        sb.append("//	----------------------------------------------------------------------------------------------------\n");
        sb.append("//	" + c_name + "\n");
        sb.append("//	----------------------------------------------------------------------------------------------------\n");
        sb.append("	private function r_" + m_name + "(msg : " + c_name + ", input : NetDataInput) : void {\n");
        sb.append(read);
        sb.append("	}\n");
        sb.append("	private function w_" + m_name + "(msg : " + c_name + ", output : NetDataOutput) : void {\n");
        sb.append(write);
        sb.append("	}\n\n");
    }
