    public IMethod getCalleeTarget(CGNode caller, CallSiteReference site, IClass receiver) {
        IMethod target = base.getCalleeTarget(caller, site, receiver);
        if (target != null && target.getReference().equals(loadFileFunRef)) {
            Set<String> names = new HashSet<String>();
            SSAInstruction call = caller.getIR().getInstructions()[caller.getIR().getCallInstructionIndices(site).intIterator().next()];
            LocalPointerKey fileNameV = new LocalPointerKey(caller, call.getUse(1));
            OrdinalSet<InstanceKey> ptrs = builder.getPointerAnalysis().getPointsToSet(fileNameV);
            for (InstanceKey k : ptrs) {
                if (k instanceof ConstantKey) {
                    Object v = ((ConstantKey) k).getValue();
                    if (v instanceof String) {
                        names.add((String) v);
                    }
                }
            }
            if (names.size() == 1) {
                String str = names.iterator().next();
                try {
                    JavaScriptLoader cl = (JavaScriptLoader) builder.getClassHierarchy().getLoader(JavaScriptTypes.jsLoader);
                    URL url = new URL(builder.getBaseURL(), str);
                    if (!loadedFiles.contains(url)) {
                        InputStream inputStream = url.openConnection().getInputStream();
                        inputStream.close();
                        JSCallGraphUtil.loadAdditionalFile(builder.getClassHierarchy(), cl, str, url);
                        loadedFiles.add(url);
                        IClass script = builder.getClassHierarchy().lookupClass(TypeReference.findOrCreate(cl.getReference(), "L" + url.getFile()));
                        return script.getMethod(JavaScriptMethods.fnSelector);
                    }
                } catch (MalformedURLException e1) {
                } catch (IOException e) {
                } catch (RuntimeException e) {
                }
            }
        }
        return target;
    }
