    public org.omg.CORBA.portable.OutputStream _invoke(String $method, org.omg.CORBA.portable.InputStream in, org.omg.CORBA.portable.ResponseHandler $rh) {
        org.omg.CORBA.portable.OutputStream out = null;
        java.lang.Integer __method = (java.lang.Integer) _methods.get($method);
        if (__method == null) throw new org.omg.CORBA.BAD_OPERATION(0, org.omg.CORBA.CompletionStatus.COMPLETED_MAYBE);
        switch(__method.intValue()) {
            case 0:
                {
                    try {
                        String serverId = org.omg.PortableInterceptor.ServerIdHelper.read(in);
                        com.sun.corba.se.PortableActivationIDL.ServerProxy serverObj = com.sun.corba.se.PortableActivationIDL.ServerProxyHelper.read(in);
                        this.registerServer(serverId, serverObj);
                        out = $rh.createReply();
                    } catch (com.sun.corba.se.PortableActivationIDL.ServerNotRegistered $ex) {
                        out = $rh.createExceptionReply();
                        com.sun.corba.se.PortableActivationIDL.ServerNotRegisteredHelper.write(out, $ex);
                    }
                    break;
                }
            case 1:
                {
                    String serverId = org.omg.PortableInterceptor.ServerIdHelper.read(in);
                    this.serverGoingDown(serverId);
                    out = $rh.createReply();
                    break;
                }
            case 2:
                {
                    try {
                        String serverId = org.omg.PortableInterceptor.ServerIdHelper.read(in);
                        String orbId = org.omg.PortableInterceptor.ORBIdHelper.read(in);
                        com.sun.corba.se.PortableActivationIDL.ORBProxy orb = com.sun.corba.se.PortableActivationIDL.ORBProxyHelper.read(in);
                        com.sun.corba.se.PortableActivationIDL.EndPointInfo endPointInfo[] = com.sun.corba.se.PortableActivationIDL.EndpointInfoListHelper.read(in);
                        this.registerORB(serverId, orbId, orb, endPointInfo);
                        out = $rh.createReply();
                    } catch (com.sun.corba.se.PortableActivationIDL.ServerNotRegistered $ex) {
                        out = $rh.createExceptionReply();
                        com.sun.corba.se.PortableActivationIDL.ServerNotRegisteredHelper.write(out, $ex);
                    } catch (com.sun.corba.se.PortableActivationIDL.NoSuchEndPoint $ex) {
                        out = $rh.createExceptionReply();
                        com.sun.corba.se.PortableActivationIDL.NoSuchEndPointHelper.write(out, $ex);
                    } catch (com.sun.corba.se.PortableActivationIDL.ORBAlreadyRegistered $ex) {
                        out = $rh.createExceptionReply();
                        com.sun.corba.se.PortableActivationIDL.ORBAlreadyRegisteredHelper.write(out, $ex);
                    }
                    break;
                }
            case 3:
                {
                    String serverId = org.omg.PortableInterceptor.ServerIdHelper.read(in);
                    String orbId = org.omg.PortableInterceptor.ORBIdHelper.read(in);
                    org.omg.PortableInterceptor.ObjectReferenceTemplate poaTemplate = org.omg.PortableInterceptor.ObjectReferenceTemplateHelper.read(in);
                    org.omg.PortableInterceptor.ObjectReferenceTemplate $result = null;
                    $result = this.registerPOA(serverId, orbId, poaTemplate);
                    out = $rh.createReply();
                    org.omg.PortableInterceptor.ObjectReferenceTemplateHelper.write(out, $result);
                    break;
                }
            case 4:
                {
                    String serverId = org.omg.PortableInterceptor.ServerIdHelper.read(in);
                    String orbId = org.omg.PortableInterceptor.ORBIdHelper.read(in);
                    org.omg.PortableInterceptor.ObjectReferenceTemplate poaTemplate = org.omg.PortableInterceptor.ObjectReferenceTemplateHelper.read(in);
                    this.poaDestroyed(serverId, orbId, poaTemplate);
                    out = $rh.createReply();
                    break;
                }
            case 5:
                {
                    try {
                        String serverId = org.omg.PortableInterceptor.ServerIdHelper.read(in);
                        this.activate(serverId);
                        out = $rh.createReply();
                    } catch (com.sun.corba.se.PortableActivationIDL.ServerAlreadyActive $ex) {
                        out = $rh.createExceptionReply();
                        com.sun.corba.se.PortableActivationIDL.ServerAlreadyActiveHelper.write(out, $ex);
                    } catch (com.sun.corba.se.PortableActivationIDL.ServerNotRegistered $ex) {
                        out = $rh.createExceptionReply();
                        com.sun.corba.se.PortableActivationIDL.ServerNotRegisteredHelper.write(out, $ex);
                    } catch (com.sun.corba.se.PortableActivationIDL.ServerHeldDown $ex) {
                        out = $rh.createExceptionReply();
                        com.sun.corba.se.PortableActivationIDL.ServerHeldDownHelper.write(out, $ex);
                    }
                    break;
                }
            case 6:
                {
                    try {
                        String serverId = org.omg.PortableInterceptor.ServerIdHelper.read(in);
                        this.shutdown(serverId);
                        out = $rh.createReply();
                    } catch (com.sun.corba.se.PortableActivationIDL.ServerNotActive $ex) {
                        out = $rh.createExceptionReply();
                        com.sun.corba.se.PortableActivationIDL.ServerNotActiveHelper.write(out, $ex);
                    } catch (com.sun.corba.se.PortableActivationIDL.ServerNotRegistered $ex) {
                        out = $rh.createExceptionReply();
                        com.sun.corba.se.PortableActivationIDL.ServerNotRegisteredHelper.write(out, $ex);
                    }
                    break;
                }
            case 7:
                {
                    try {
                        String serverId = org.omg.PortableInterceptor.ServerIdHelper.read(in);
                        this.install(serverId);
                        out = $rh.createReply();
                    } catch (com.sun.corba.se.PortableActivationIDL.ServerNotRegistered $ex) {
                        out = $rh.createExceptionReply();
                        com.sun.corba.se.PortableActivationIDL.ServerNotRegisteredHelper.write(out, $ex);
                    } catch (com.sun.corba.se.PortableActivationIDL.ServerHeldDown $ex) {
                        out = $rh.createExceptionReply();
                        com.sun.corba.se.PortableActivationIDL.ServerHeldDownHelper.write(out, $ex);
                    } catch (com.sun.corba.se.PortableActivationIDL.ServerAlreadyInstalled $ex) {
                        out = $rh.createExceptionReply();
                        com.sun.corba.se.PortableActivationIDL.ServerAlreadyInstalledHelper.write(out, $ex);
                    }
                    break;
                }
            case 8:
                {
                    try {
                        String serverId = org.omg.PortableInterceptor.ServerIdHelper.read(in);
                        this.uninstall(serverId);
                        out = $rh.createReply();
                    } catch (com.sun.corba.se.PortableActivationIDL.ServerNotRegistered $ex) {
                        out = $rh.createExceptionReply();
                        com.sun.corba.se.PortableActivationIDL.ServerNotRegisteredHelper.write(out, $ex);
                    } catch (com.sun.corba.se.PortableActivationIDL.ServerHeldDown $ex) {
                        out = $rh.createExceptionReply();
                        com.sun.corba.se.PortableActivationIDL.ServerHeldDownHelper.write(out, $ex);
                    } catch (com.sun.corba.se.PortableActivationIDL.ServerAlreadyUninstalled $ex) {
                        out = $rh.createExceptionReply();
                        com.sun.corba.se.PortableActivationIDL.ServerAlreadyUninstalledHelper.write(out, $ex);
                    }
                    break;
                }
            case 9:
                {
                    String $result[] = null;
                    $result = this.getActiveServers();
                    out = $rh.createReply();
                    com.sun.corba.se.PortableActivationIDL.ServerIdsHelper.write(out, $result);
                    break;
                }
            case 10:
                {
                    try {
                        String serverId = org.omg.PortableInterceptor.ServerIdHelper.read(in);
                        String $result[] = null;
                        $result = this.getORBNames(serverId);
                        out = $rh.createReply();
                        com.sun.corba.se.PortableActivationIDL.ORBidListHelper.write(out, $result);
                    } catch (com.sun.corba.se.PortableActivationIDL.ServerNotRegistered $ex) {
                        out = $rh.createExceptionReply();
                        com.sun.corba.se.PortableActivationIDL.ServerNotRegisteredHelper.write(out, $ex);
                    }
                    break;
                }
            case 11:
                {
                    String serverId = org.omg.PortableInterceptor.ServerIdHelper.read(in);
                    String orbId = org.omg.PortableInterceptor.ORBIdHelper.read(in);
                    String orbAdapterName[] = org.omg.PortableInterceptor.AdapterNameHelper.read(in);
                    org.omg.PortableInterceptor.ObjectReferenceTemplate $result = null;
                    $result = this.lookupPOATemplate(serverId, orbId, orbAdapterName);
                    out = $rh.createReply();
                    org.omg.PortableInterceptor.ObjectReferenceTemplateHelper.write(out, $result);
                    break;
                }
            case 12:
                {
                    try {
                        String serverId = org.omg.PortableInterceptor.ServerIdHelper.read(in);
                        String endPoint = in.read_string();
                        com.sun.corba.se.PortableActivationIDL.LocatorPackage.ServerLocationPerType $result = null;
                        $result = this.locateServer(serverId, endPoint);
                        out = $rh.createReply();
                        com.sun.corba.se.PortableActivationIDL.LocatorPackage.ServerLocationPerTypeHelper.write(out, $result);
                    } catch (com.sun.corba.se.PortableActivationIDL.NoSuchEndPoint $ex) {
                        out = $rh.createExceptionReply();
                        com.sun.corba.se.PortableActivationIDL.NoSuchEndPointHelper.write(out, $ex);
                    } catch (com.sun.corba.se.PortableActivationIDL.ServerNotRegistered $ex) {
                        out = $rh.createExceptionReply();
                        com.sun.corba.se.PortableActivationIDL.ServerNotRegisteredHelper.write(out, $ex);
                    } catch (com.sun.corba.se.PortableActivationIDL.ServerHeldDown $ex) {
                        out = $rh.createExceptionReply();
                        com.sun.corba.se.PortableActivationIDL.ServerHeldDownHelper.write(out, $ex);
                    }
                    break;
                }
            case 13:
                {
                    try {
                        String serverId = org.omg.PortableInterceptor.ServerIdHelper.read(in);
                        String orbId = org.omg.PortableInterceptor.ORBIdHelper.read(in);
                        com.sun.corba.se.PortableActivationIDL.LocatorPackage.ServerLocationPerORB $result = null;
                        $result = this.locateServerForORB(serverId, orbId);
                        out = $rh.createReply();
                        com.sun.corba.se.PortableActivationIDL.LocatorPackage.ServerLocationPerORBHelper.write(out, $result);
                    } catch (com.sun.corba.se.PortableActivationIDL.InvalidORBid $ex) {
                        out = $rh.createExceptionReply();
                        com.sun.corba.se.PortableActivationIDL.InvalidORBidHelper.write(out, $ex);
                    } catch (com.sun.corba.se.PortableActivationIDL.ServerNotRegistered $ex) {
                        out = $rh.createExceptionReply();
                        com.sun.corba.se.PortableActivationIDL.ServerNotRegisteredHelper.write(out, $ex);
                    } catch (com.sun.corba.se.PortableActivationIDL.ServerHeldDown $ex) {
                        out = $rh.createExceptionReply();
                        com.sun.corba.se.PortableActivationIDL.ServerHeldDownHelper.write(out, $ex);
                    }
                    break;
                }
            case 14:
                {
                    try {
                        String endPointType = in.read_string();
                        int $result = (int) 0;
                        $result = this.getEndpoint(endPointType);
                        out = $rh.createReply();
                        out.write_long($result);
                    } catch (com.sun.corba.se.PortableActivationIDL.NoSuchEndPoint $ex) {
                        out = $rh.createExceptionReply();
                        com.sun.corba.se.PortableActivationIDL.NoSuchEndPointHelper.write(out, $ex);
                    }
                    break;
                }
            case 15:
                {
                    try {
                        com.sun.corba.se.PortableActivationIDL.LocatorPackage.ServerLocationPerORB location = com.sun.corba.se.PortableActivationIDL.LocatorPackage.ServerLocationPerORBHelper.read(in);
                        String endPointType = in.read_string();
                        int $result = (int) 0;
                        $result = this.getServerPortForType(location, endPointType);
                        out = $rh.createReply();
                        out.write_long($result);
                    } catch (com.sun.corba.se.PortableActivationIDL.NoSuchEndPoint $ex) {
                        out = $rh.createExceptionReply();
                        com.sun.corba.se.PortableActivationIDL.NoSuchEndPointHelper.write(out, $ex);
                    }
                    break;
                }
            default:
                throw new org.omg.CORBA.BAD_OPERATION(0, org.omg.CORBA.CompletionStatus.COMPLETED_MAYBE);
        }
        return out;
    }
