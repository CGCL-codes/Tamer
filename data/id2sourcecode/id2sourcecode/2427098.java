        public void run() {
            while (true) {
                AbstractCodeBlockWrapper target = null;
                synchronized (remoteClassQueue) {
                    while (remoteClassQueue.size() == 0) {
                        if (!running) return;
                        try {
                            remoteClassQueue.wait(200);
                        } catch (Exception e) {
                        }
                    }
                    target = (AbstractCodeBlockWrapper) remoteClassQueue.removeFirst();
                }
                CodeBlock src = target.getTargetBlock();
                if (src instanceof ReplacementBlockTrigger) continue;
                CodeBlock result = null;
                InstructionSource blockCodes = null;
                String classStem = null;
                boolean realMode = false;
                if (src instanceof RealModeUBlock) {
                    blockCodes = ((RealModeUBlock) src).getAsInstructionSource();
                    classStem = "org.jpc.dynamic.R";
                    realMode = true;
                } else if (src instanceof ProtectedModeUBlock) {
                    blockCodes = ((ProtectedModeUBlock) src).getAsInstructionSource();
                    classStem = "org.jpc.dynamic.P";
                }
                try {
                    ByteArrayOutputStream bout = new ByteArrayOutputStream();
                    DataOutputStream dout = new DataOutputStream(bout);
                    while (blockCodes.getNext()) {
                        int length = blockCodes.getLength();
                        dout.writeInt(length);
                        dout.writeInt(blockCodes.getX86Length());
                        for (int i = 0; i < length; i++) {
                            int uCode = blockCodes.getMicrocode();
                            dout.writeInt(uCode);
                        }
                    }
                    byte[] rawCodeBlock = bout.toByteArray();
                    int hash = -1;
                    for (int i = 0; i < rawCodeBlock.length; i++) {
                        hash ^= rawCodeBlock[i];
                        hash *= 31;
                    }
                    Integer key = Integer.valueOf(hash);
                    byte[] existing = null;
                    if (realMode) existing = (byte[]) realBlockCache.get(key); else existing = (byte[]) protectedBlockCache.get(key);
                    if (Arrays.equals(rawCodeBlock, existing)) {
                        if (realMode) result = (CodeBlock) (((Class) realClassCache.get(key)).newInstance()); else result = (CodeBlock) (((Class) protectedClassCache.get(key)).newInstance());
                    } else {
                        URL url = serverURI.resolve(classStem).toURL();
                        URLConnection conn = url.openConnection();
                        conn.setDoOutput(true);
                        conn.setRequestProperty("Content-Length", String.valueOf(rawCodeBlock.length));
                        conn.setUseCaches(false);
                        conn.getOutputStream().write(rawCodeBlock);
                        StringBuffer buf = new StringBuffer();
                        InputStream in = conn.getInputStream();
                        while (true) {
                            int ch = in.read();
                            if (ch < 0) break;
                            buf.append((char) ch);
                        }
                        in.close();
                        String className = buf.toString();
                        if (className.length() == 0) throw new IllegalStateException("No class");
                        Class cls = urlClassLoader.loadClass(className);
                        result = (CodeBlock) cls.newInstance();
                        if (realMode) {
                            realClassCache.put(key, cls);
                            realBlockCache.put(key, rawCodeBlock);
                        } else {
                            protectedClassCache.put(key, cls);
                            protectedBlockCache.put(key, rawCodeBlock);
                        }
                    }
                } catch (Throwable e) {
                    System.out.println(">>> " + e);
                }
                if (result != null) target.setTargetBlock(new ReplacementBlockTrigger(result)); else target.setTargetBlock(new ReplacementBlockTrigger(src));
            }
        }
