    private void writeInvocation(WSNode node, boolean thread, PrintWriter pw) {
        String id = node.getID();
        String wsdlID = getWSDLID(node);
        WSComponent component = node.getComponent();
        QName portTypeQName = component.getPortTypeQName();
        String operation = component.getOperationName();
        pw.println(TAB + "# Invoke " + id + ".");
        pw.println(TAB + id + QNAME_SUFFIX + " = QName('" + portTypeQName.getNamespaceURI() + "', '" + portTypeQName.getLocalPart() + "')");
        pw.println(TAB + wsdlID + " = " + PROPERTIES_VARIABLE + "." + GET_PROPERTY_METHOD + "('" + wsdlID + "')");
        pw.println(TAB + id + INVOKER_SUFFIX + " = " + INVOKER_CLASS + "(" + id + QNAME_SUFFIX + ", " + wsdlID + ", '" + id + "',");
        pw.println(TAB + TAB + MESSAGE_BOX_URL_VARIABLE + ", " + GFAC_VARIABLE + ", " + NOTIFICATION_VARIABLE + ")");
        pw.println(TAB + "def " + INVOKE_METHOD + id + "():");
        pw.println(TAB + TAB + id + INVOKER_SUFFIX + "." + SETUP_METHOD + "()");
        pw.println(TAB + TAB + id + INVOKER_SUFFIX + "." + SET_OPERATION_METHOD + "('" + operation + "')");
        for (Port port : node.getInputPorts()) {
            String portName = port.getName();
            String value;
            Node fromNode = port.getFromNode();
            if (fromNode instanceof InputNode) {
                value = PROPERTIES_VARIABLE + "." + GET_PROPERTY_METHOD + "('" + fromNode.getID() + "')";
            } else {
                Port fromPort = port.getFromPort();
                value = "" + fromNode.getID() + INVOKER_SUFFIX + "." + GET_OUTPUT_METHOD + "('" + fromPort.getName() + "')";
                this.executingNodes.remove(fromNode);
            }
            pw.println(TAB + TAB + portName + VALUE_SUFFIX + " = " + value);
            pw.println(TAB + TAB + id + INVOKER_SUFFIX + "." + SET_INPUT_METHOD + "('" + portName + "', " + portName + VALUE_SUFFIX + ")");
        }
        pw.println(TAB + TAB + "print 'Invoking " + id + ".'");
        pw.println(TAB + TAB + id + INVOKER_SUFFIX + "." + INVOKE_METHOD + "()");
        if (thread) {
            pw.println(TAB + "thread.start_new_thread(" + INVOKE_METHOD + id + ", ())");
        } else {
            pw.println(TAB + INVOKE_METHOD + id + "()");
        }
        pw.println();
        this.executingNodes.add(node);
    }
