    protected Object callMethod(String name, long primitivesFlag, Object[] parameters) throws RemoteException {
        String dot = "__";
        name = TextUtil.replace(name, dot, ".");
        StringBuffer methodBuffer = new StringBuffer();
        methodBuffer.append("<?xml version=\"1.0\"?>").append("<methodCall>").append("<methodName>").append(name).append("</methodName>");
        if (parameters != null && parameters.length > 0) {
            methodBuffer.append("<params>");
            for (int i = 0; i < parameters.length; i++) {
                methodBuffer.append("<param><value>");
                Object object = parameters[i];
                try {
                    XmlRpcSerializer.serialize(methodBuffer, object);
                } catch (IOException e) {
                    de.enough.polish.util.Debug.debug("error", "de.enough.polish.rmi.xmlrpc.XmlRpcRemoteClient", 105, "Unable to serialize " + object, e);
                    throw new RemoteException(e);
                }
                methodBuffer.append("</value></param>");
            }
            methodBuffer.append("</params>");
        }
        methodBuffer.append("</methodCall>");
        byte[] methodData = methodBuffer.toString().getBytes();
        HttpConnection connection = null;
        DataOutputStream out = null;
        DataInputStream in = null;
        try {
            connection = new RedirectHttpConnection(this.url);
            connection.setRequestMethod(HttpConnection.POST);
            connection.setRequestProperty("Content-Type", "text/xml");
            connection.setRequestProperty("Content-Length", Integer.toString(methodData.length));
            if (this.cookie != null) {
                connection.setRequestProperty("cookie", this.cookie);
            }
            out = connection.openDataOutputStream();
            out.write(methodData);
            in = connection.openDataInputStream();
            int status = connection.getResponseCode();
            if (status != HttpConnection.HTTP_OK) {
                throw new RemoteException("Server responded with response code " + status);
            } else {
                try {
                    out.flush();
                } catch (IllegalStateException e) {
                }
                String newCookie = connection.getHeaderField("Set-cookie");
                if (newCookie != null) {
                    int semicolonPos = newCookie.indexOf(';');
                    if (semicolonPos != -1) {
                        newCookie = newCookie.substring(0, semicolonPos);
                    }
                    this.cookie = newCookie;
                }
                ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
                byte[] readBuffer = new byte[8 * 1024];
                int read;
                while ((read = in.read(readBuffer)) != -1) {
                    byteOut.write(readBuffer, 0, read);
                }
                String response = new String(byteOut.toByteArray());
                XmlDomNode root = XmlDomParser.parseTree(response);
                XmlDomNode node;
                if ("methodResponse".equals(root.getName())) {
                    node = root;
                } else {
                    node = root.getChild("methodResponse");
                    if (node == null) {
                        throw new IOException("Invalid XML RPC Response: " + response);
                    }
                }
                node = node.getChild(0);
                if (node.getName().equals("fault")) {
                    node = node.getChild("value");
                    Hashtable struct = (Hashtable) XmlRpcSerializer.deserialize(node);
                    int faultCode = -1;
                    Integer faultCodeInt = (Integer) struct.get("faultCode");
                    if (faultCodeInt != null) {
                        faultCode = faultCodeInt.intValue();
                    }
                    String message = (String) struct.get("faultString");
                    throw new XmlRpcRemoteException(faultCode, message);
                } else {
                    node = node.getChild("param").getChild("value");
                    return XmlRpcSerializer.deserialize(node);
                }
            }
        } catch (IOException e) {
            throw new RemoteException(e);
        } catch (Throwable e) {
            de.enough.polish.util.Debug.debug("error", "de.enough.polish.rmi.xmlrpc.XmlRpcRemoteClient", 199, "Unexpected error during XML RPC call: ", e);
            throw new RemoteException(e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (Exception e) {
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (Exception e) {
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                    connection = null;
                } catch (Exception e) {
                }
            }
        }
    }
