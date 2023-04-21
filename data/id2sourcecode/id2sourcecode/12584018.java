    public org.omg.CORBA.portable.OutputStream _invoke(String $method, org.omg.CORBA.portable.InputStream in, org.omg.CORBA.portable.ResponseHandler $rh) {
        org.omg.CORBA.portable.OutputStream out = null;
        java.lang.Integer __method = (java.lang.Integer) _methods.get($method);
        if (__method == null) throw new org.omg.CORBA.BAD_OPERATION(0, org.omg.CORBA.CompletionStatus.COMPLETED_MAYBE);
        switch(__method.intValue()) {
            case 0:
                {
                    try {
                        com.sun.corba.se.PortableActivationIDL.RepositoryPackage.ServerDef serverDef = com.sun.corba.se.PortableActivationIDL.RepositoryPackage.ServerDefHelper.read(in);
                        String $result = null;
                        $result = this.registerServer(serverDef);
                        out = $rh.createReply();
                        out.write_string($result);
                    } catch (com.sun.corba.se.PortableActivationIDL.ServerAlreadyRegistered $ex) {
                        out = $rh.createExceptionReply();
                        com.sun.corba.se.PortableActivationIDL.ServerAlreadyRegisteredHelper.write(out, $ex);
                    } catch (com.sun.corba.se.PortableActivationIDL.BadServerDefinition $ex) {
                        out = $rh.createExceptionReply();
                        com.sun.corba.se.PortableActivationIDL.BadServerDefinitionHelper.write(out, $ex);
                    }
                    break;
                }
            case 1:
                {
                    try {
                        String serverId = org.omg.PortableInterceptor.ServerIdHelper.read(in);
                        this.unregisterServer(serverId);
                        out = $rh.createReply();
                    } catch (com.sun.corba.se.PortableActivationIDL.ServerNotRegistered $ex) {
                        out = $rh.createExceptionReply();
                        com.sun.corba.se.PortableActivationIDL.ServerNotRegisteredHelper.write(out, $ex);
                    }
                    break;
                }
            case 2:
                {
                    try {
                        String serverId = org.omg.PortableInterceptor.ServerIdHelper.read(in);
                        com.sun.corba.se.PortableActivationIDL.RepositoryPackage.ServerDef $result = null;
                        $result = this.getServer(serverId);
                        out = $rh.createReply();
                        com.sun.corba.se.PortableActivationIDL.RepositoryPackage.ServerDefHelper.write(out, $result);
                    } catch (com.sun.corba.se.PortableActivationIDL.ServerNotRegistered $ex) {
                        out = $rh.createExceptionReply();
                        com.sun.corba.se.PortableActivationIDL.ServerNotRegisteredHelper.write(out, $ex);
                    }
                    break;
                }
            case 3:
                {
                    try {
                        String serverId = org.omg.PortableInterceptor.ServerIdHelper.read(in);
                        boolean $result = false;
                        $result = this.isInstalled(serverId);
                        out = $rh.createReply();
                        out.write_boolean($result);
                    } catch (com.sun.corba.se.PortableActivationIDL.ServerNotRegistered $ex) {
                        out = $rh.createExceptionReply();
                        com.sun.corba.se.PortableActivationIDL.ServerNotRegisteredHelper.write(out, $ex);
                    }
                    break;
                }
            case 4:
                {
                    try {
                        String serverId = org.omg.PortableInterceptor.ServerIdHelper.read(in);
                        this.install(serverId);
                        out = $rh.createReply();
                    } catch (com.sun.corba.se.PortableActivationIDL.ServerNotRegistered $ex) {
                        out = $rh.createExceptionReply();
                        com.sun.corba.se.PortableActivationIDL.ServerNotRegisteredHelper.write(out, $ex);
                    } catch (com.sun.corba.se.PortableActivationIDL.ServerAlreadyInstalled $ex) {
                        out = $rh.createExceptionReply();
                        com.sun.corba.se.PortableActivationIDL.ServerAlreadyInstalledHelper.write(out, $ex);
                    }
                    break;
                }
            case 5:
                {
                    try {
                        String serverId = org.omg.PortableInterceptor.ServerIdHelper.read(in);
                        this.uninstall(serverId);
                        out = $rh.createReply();
                    } catch (com.sun.corba.se.PortableActivationIDL.ServerNotRegistered $ex) {
                        out = $rh.createExceptionReply();
                        com.sun.corba.se.PortableActivationIDL.ServerNotRegisteredHelper.write(out, $ex);
                    } catch (com.sun.corba.se.PortableActivationIDL.ServerAlreadyUninstalled $ex) {
                        out = $rh.createExceptionReply();
                        com.sun.corba.se.PortableActivationIDL.ServerAlreadyUninstalledHelper.write(out, $ex);
                    }
                    break;
                }
            case 6:
                {
                    String $result[] = null;
                    $result = this.listRegisteredServers();
                    out = $rh.createReply();
                    com.sun.corba.se.PortableActivationIDL.ServerIdsHelper.write(out, $result);
                    break;
                }
            case 7:
                {
                    String $result[] = null;
                    $result = this.getApplicationNames();
                    out = $rh.createReply();
                    com.sun.corba.se.PortableActivationIDL.RepositoryPackage.AppNamesHelper.write(out, $result);
                    break;
                }
            case 8:
                {
                    try {
                        String applicationName = in.read_string();
                        String $result = null;
                        $result = this.getServerID(applicationName);
                        out = $rh.createReply();
                        out.write_string($result);
                    } catch (com.sun.corba.se.PortableActivationIDL.ServerNotRegistered $ex) {
                        out = $rh.createExceptionReply();
                        com.sun.corba.se.PortableActivationIDL.ServerNotRegisteredHelper.write(out, $ex);
                    }
                    break;
                }
            default:
                throw new org.omg.CORBA.BAD_OPERATION(0, org.omg.CORBA.CompletionStatus.COMPLETED_MAYBE);
        }
        return out;
    }
