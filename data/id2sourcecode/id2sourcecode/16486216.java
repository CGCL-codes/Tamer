        public OverloadedChecksum(Krb5Context context, Credentials tgt, Credentials serviceTicket) throws KrbException, IOException, GSSException {
            byte[] krbCredMessage = null;
            int pos = 0;
            int size = CHECKSUM_LENGTH_SIZE + CHECKSUM_BINDINGS_SIZE + CHECKSUM_FLAGS_SIZE;
            if (!tgt.isForwardable()) {
                context.setCredDelegState(false);
                context.setDelegPolicyState(false);
            } else if (context.getCredDelegState()) {
                if (context.getDelegPolicyState()) {
                    if (!serviceTicket.checkDelegate()) {
                        context.setDelegPolicyState(false);
                    }
                }
            } else if (context.getDelegPolicyState()) {
                if (serviceTicket.checkDelegate()) {
                    context.setCredDelegState(true);
                } else {
                    context.setDelegPolicyState(false);
                }
            }
            if (context.getCredDelegState()) {
                KrbCred krbCred = null;
                CipherHelper cipherHelper = context.getCipherHelper(serviceTicket.getSessionKey());
                if (useNullKey(cipherHelper)) {
                    krbCred = new KrbCred(tgt, serviceTicket, EncryptionKey.NULL_KEY);
                } else {
                    krbCred = new KrbCred(tgt, serviceTicket, serviceTicket.getSessionKey());
                }
                krbCredMessage = krbCred.getMessage();
                size += CHECKSUM_DELEG_OPT_SIZE + CHECKSUM_DELEG_LGTH_SIZE + krbCredMessage.length;
            }
            checksumBytes = new byte[size];
            checksumBytes[pos++] = CHECKSUM_FIRST_BYTES[0];
            checksumBytes[pos++] = CHECKSUM_FIRST_BYTES[1];
            checksumBytes[pos++] = CHECKSUM_FIRST_BYTES[2];
            checksumBytes[pos++] = CHECKSUM_FIRST_BYTES[3];
            ChannelBinding localBindings = context.getChannelBinding();
            if (localBindings != null) {
                byte[] localBindingsBytes = computeChannelBinding(context.getChannelBinding());
                System.arraycopy(localBindingsBytes, 0, checksumBytes, pos, localBindingsBytes.length);
            }
            pos += CHECKSUM_BINDINGS_SIZE;
            if (context.getCredDelegState()) flags |= CHECKSUM_DELEG_FLAG;
            if (context.getMutualAuthState()) flags |= CHECKSUM_MUTUAL_FLAG;
            if (context.getReplayDetState()) flags |= CHECKSUM_REPLAY_FLAG;
            if (context.getSequenceDetState()) flags |= CHECKSUM_SEQUENCE_FLAG;
            if (context.getIntegState()) flags |= CHECKSUM_INTEG_FLAG;
            if (context.getConfState()) flags |= CHECKSUM_CONF_FLAG;
            byte[] temp = new byte[4];
            writeLittleEndian(flags, temp);
            checksumBytes[pos++] = temp[0];
            checksumBytes[pos++] = temp[1];
            checksumBytes[pos++] = temp[2];
            checksumBytes[pos++] = temp[3];
            if (context.getCredDelegState()) {
                PrincipalName delegateTo = serviceTicket.getServer();
                StringBuffer buf = new StringBuffer("\"");
                buf.append(delegateTo.getName()).append('\"');
                String realm = delegateTo.getRealmAsString();
                buf.append(" \"krbtgt/").append(realm).append('@');
                buf.append(realm).append('\"');
                SecurityManager sm = System.getSecurityManager();
                if (sm != null) {
                    DelegationPermission perm = new DelegationPermission(buf.toString());
                    sm.checkPermission(perm);
                }
                checksumBytes[pos++] = (byte) 0x01;
                checksumBytes[pos++] = (byte) 0x00;
                if (krbCredMessage.length > 0x0000ffff) throw new GSSException(GSSException.FAILURE, -1, "Incorrect messsage length");
                writeLittleEndian(krbCredMessage.length, temp);
                checksumBytes[pos++] = temp[0];
                checksumBytes[pos++] = temp[1];
                System.arraycopy(krbCredMessage, 0, checksumBytes, pos, krbCredMessage.length);
            }
        }
