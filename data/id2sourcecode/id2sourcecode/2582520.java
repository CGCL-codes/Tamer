    static Object read_instance(InputStream input, final int position, Object value, int value_tag, BoxedValueHelper helper, String id, String[] ids, String codebase) {
        if (helper != m_StringValueHelper && id != null) if (id.equals(StringValueHelper.id())) {
            value = null;
            helper = m_StringValueHelper;
        }
        try {
            if ((value_tag & vf_CHUNKING) != 0) {
                BufferedCdrOutput output = createBuffer(input, 1024);
                readNestedValue(value_tag, input, output, -1);
                BufferredCdrInput ci = new BufferredCdrInput(output.buffer.getBuffer());
                ci.setRunTime(output.getRunTime());
                input = new HeadlessInput(ci, input);
            } else {
                if (input instanceof BufferredCdrInput) {
                    input = new HeadlessInput((BufferredCdrInput) input, null);
                } else if (input instanceof HeadlessInput) {
                    ((HeadlessInput) input).subsequentCalls = false;
                } else {
                    BufferedCdrOutput bout = new BufferedCdrOutput();
                    int c;
                    while ((c = input.read()) >= 0) bout.write((byte) c);
                    input = new HeadlessInput((BufferredCdrInput) bout.create_input_stream(), input);
                }
            }
        } catch (IOException ex) {
            MARSHAL m = new MARSHAL("Unable to read chunks");
            m.minor = Minor.Value;
            m.initCause(ex);
            throw m;
        }
        return readValue(input, position, value, helper, id, ids, codebase);
    }
