    private void generateCppPeerMappings(Writer output) throws IOException {
        String fullName = metaClass.getFullyQualifiedTrueName(".");
        MetaClass peerMetaClass = metaClass.toPeer();
        String fullPeerName = "::" + peerMetaClass.getFullyQualifiedName("::");
        String className = mangleName(metaClass.getFullyQualifiedTrueName("/"));
        Util.generateComment(output, "These JNI mappings are for the Jace Peer for " + fullName + "." + newLine + "Please do not edit these JNI mappings. Any changes made will be overwritten." + newLine + newLine + "For more information, please refer to the Jace Developer's Guide.");
        output.write(newLine);
        ProxyGenerator proxy = new ProxyGenerator.Builder(new ClassPath(System.getProperty("java.class.path")), classFile, new ProxyGenerator.AcceptAll()).accessibility(AccessibilityType.PRIVATE).build();
        proxy.includeStandardHeaders(output, true);
        output.write("#include \"" + JaceConstants.getProxyPackage().asPath() + "/java/lang/Throwable.h\"" + newLine);
        output.write("#include \"" + JaceConstants.getProxyPackage().asPath() + "/java/lang/RuntimeException.h\"" + newLine);
        output.write("#include \"" + JaceConstants.getProxyPackage().asPath() + "/java/lang/Class.h\"" + newLine);
        output.write("#include \"" + JaceConstants.getProxyPackage().asPath() + "/java/lang/String.h\"" + newLine);
        output.write("#include \"jace/Jace.h\"" + newLine);
        output.write("#include \"jace/VirtualMachineRunningError.h\"" + newLine);
        output.write(newLine);
        for (MetaClass dependency : getDependencies(classFile)) output.write(dependency.include() + newLine);
        output.write(metaClass.toPeer().include() + newLine);
        output.write(newLine);
        output.write("#include <iostream>" + newLine);
        output.write("#include <assert.h>" + newLine);
        output.write(newLine);
        for (ClassMethod method : classFile.getMethods()) {
            if (!method.getAccessFlags().contains(MethodAccessFlag.NATIVE)) continue;
            String methodName = method.getName();
            Util.generateComment(output, "The JNI mapping for" + newLine + newLine + "Class: " + mangleName(metaClass.getFullyQualifiedTrueName("/")) + newLine + "Method: " + method.getName() + newLine + "Signature: " + method.getDescriptor());
            if (methodName.equals("jaceCreateInstance")) {
                output.write("extern \"C\" JNIEXPORT jlong JNICALL ");
                output.write("Java_" + className + "_jaceCreateInstance(JNIEnv *env, jobject jPeer)" + newLine);
                output.write("{" + newLine);
                output.write("  try" + newLine);
                output.write("  {" + newLine);
                output.write("    ::jace::Peer* peer = new " + fullPeerName + "(jPeer);" + newLine);
                output.write("    peer->initialize(); " + newLine);
                output.write("    return reinterpret_cast<jlong>(peer);" + newLine);
                output.write("  }" + newLine);
                output.write("  catch (jace::proxy::java::lang::Throwable& t)" + newLine);
                output.write("  {" + newLine);
                output.write("    env->Throw(static_cast<jthrowable>(env->NewLocalRef(static_cast<jobject>(t))));" + newLine);
                output.write("    ::jace::detach();" + newLine);
                output.write("    return 0;" + newLine);
                output.write("  }" + newLine);
                output.write("  catch (std::exception& e)" + newLine);
                output.write("  {" + newLine);
                output.write("    std::string msg = std::string(\"An unexpected JNI error has occurred: \") + e.what();" + newLine);
                output.write("    jace::proxy::java::lang::RuntimeException ex(jace::java_new<jace::proxy::java::lang::RuntimeException>(msg));" + newLine);
                output.write("    env->Throw(static_cast<jthrowable>(env->NewLocalRef(static_cast<jobject>(ex))));" + newLine);
                output.write("    ::jace::detach();" + newLine);
                output.write("    return 0;" + newLine);
                output.write("  }" + newLine);
                output.write("}" + newLine);
                output.write(newLine);
                continue;
            } else if (methodName.equals("jaceDestroyInstance")) {
                output.write("extern \"C\" JNIEXPORT void JNICALL ");
                output.write("Java_" + className + "_jaceDestroyInstance(JNIEnv *env, jobject jPeer)" + newLine);
                output.write("{" + newLine);
                output.write("  try" + newLine);
                output.write("  {" + newLine);
                output.write("    jclass classId = env->GetObjectClass(jPeer);" + newLine);
                output.write("    jfieldID fieldId = env->GetFieldID(classId, \"jaceNativeHandle\", \"J\");" + newLine);
                output.write("    if (fieldId == 0)" + newLine);
                output.write("      ::jace::catchAndThrow();" + newLine);
                output.write("    jlong nativeHandle = env->GetLongField(jPeer, fieldId);" + newLine);
                output.write("    ::jace::Peer* peer = reinterpret_cast<::jace::Peer*>(nativeHandle);" + newLine);
                output.write("    peer->destroy();" + newLine);
                output.write("    delete peer; " + newLine);
                output.write("  }" + newLine);
                output.write("  catch (std::exception& e)" + newLine);
                output.write("  {" + newLine);
                output.write("    std::cerr << std::string(\"An unexpected JNI error has occurred: \") + e.what();" + newLine);
                output.write("  }" + newLine);
                output.write("}" + newLine);
                output.write(newLine);
                continue;
            } else if (methodName.equals("jaceSetVm")) {
                output.write("extern \"C\" JNIEXPORT void JNICALL ");
                output.write("Java_" + className + "_jaceSetVm(JNIEnv *env, jclass)" + newLine);
                output.write("{" + newLine);
                output.write("  try" + newLine);
                output.write("  {" + newLine);
                output.write("    if (!::jace::getJavaVm())" + newLine);
                output.write("    {" + newLine);
                output.write("      JavaVM* jvm;" + newLine);
                output.write("      jint result = env->GetJavaVM(&jvm);" + newLine);
                output.write(newLine);
                output.write("      if (result != 0)" + newLine);
                output.write("      {" + newLine);
                output.write("        std::string msg = std::string(\"" + className + " ::jaceSetVm\\n\") + " + newLine);
                output.write("          \"Unable to retrieve the JVM from the JNIEnv* object. The specific JNI error code is \" +" + newLine);
                output.write("          ::jace::toString(result);" + newLine);
                output.write("        throw ::jace::JNIException(msg);" + newLine);
                output.write("      }" + newLine);
                output.write("      ::jace::setJavaVm(jvm);" + newLine);
                output.write("    }" + newLine);
                output.write(newLine);
                output.write("  }" + newLine);
                output.write("  catch (jace::VirtualMachineRunningError&)" + newLine);
                output.write("  {" + newLine);
                output.write("    return; // the VM is already set, we're done" + newLine);
                output.write("  }" + newLine);
                output.write("  catch (std::exception& e)" + newLine);
                output.write("  {" + newLine);
                output.write("    std::string msg = std::string(\"An unexpected JNI error has occurred: \") + e.what();" + newLine);
                output.write("    std::cerr << msg;" + newLine);
                output.write("    return;" + newLine);
                output.write("  }" + newLine);
                output.write("}" + newLine);
                output.write(newLine);
                output.write(newLine);
                output.write(newLine);
                continue;
            }
            String functionName = getNativeMethodName(metaClass, method);
            MetaClass returnType = MetaClassFactory.getMetaClass(method.getReturnType()).proxy();
            boolean isStatic = method.getAccessFlags().contains(MethodAccessFlag.STATIC);
            List<TypeName> parameterTypes = method.getParameterTypes();
            Collection<String> params = Lists.newArrayListWithCapacity(1 + parameterTypes.size());
            if (isStatic) params.add("jclass jP0"); else params.add("jobject jP0");
            int parameterIndex = 1;
            for (TypeName param : parameterTypes) {
                MetaClass paramClass = MetaClassFactory.getMetaClass(param).proxy();
                params.add(paramClass.getJniType() + " jP" + parameterIndex);
                ++parameterIndex;
            }
            output.write("extern \"C\" JNIEXPORT " + returnType.getJniType() + " JNICALL " + functionName);
            output.write("(JNIEnv* env, ");
            output.write(new DelimitedCollection<String>(params).toString(", "));
            output.write(") { " + newLine);
            output.write(newLine);
            output.write("  try" + newLine);
            output.write("  {" + newLine);
            String target;
            if (!isStatic) {
                output.write("    " + fullPeerName + "* peer = dynamic_cast< " + fullPeerName + "*>(::jace::getPeer(jP0));" + newLine);
                output.write("    assert(peer!=0);" + newLine);
                target = "peer->";
            } else target = fullPeerName + "::";
            parameterIndex = 1;
            for (TypeName param : parameterTypes) {
                MetaClass paramClass = MetaClassFactory.getMetaClass(param).proxy();
                output.write("    " + "::" + paramClass.getFullyQualifiedName("::"));
                output.write(" p" + parameterIndex + "(jP" + parameterIndex + ");" + newLine);
                ++parameterIndex;
            }
            if (parameterTypes.size() > 0 || !isStatic) output.write(newLine);
            if (returnType instanceof VoidClass) {
                output.write("    " + target + methodName + "(");
                for (int i = 0; i < params.size() - 1; ++i) {
                    output.write("p" + (i + 1));
                    if (i < params.size() - 2) output.write(",");
                    output.write(" ");
                }
                output.write(");" + newLine);
                output.write("    return;" + newLine);
            } else {
                output.write("    return ");
                if (!returnType.isPrimitive()) output.write("static_cast<" + returnType.getJniType() + ">(env->NewLocalRef(");
                output.write(target + methodName + "(");
                for (int i = 0; i < params.size() - 1; ++i) {
                    output.write("p" + (i + 1));
                    if (i < params.size() - 2) output.write(", ");
                }
                output.write(")");
                if (!returnType.isPrimitive()) output.write("))");
                output.write(";" + newLine);
            }
            output.write("  }" + newLine);
            String returnValue = returnType instanceof VoidClass ? "" : " NULL";
            output.write("  catch (jace::proxy::java::lang::Throwable& t)" + newLine);
            output.write("  {" + newLine);
            output.write("    env->Throw(static_cast<jthrowable>(env->NewLocalRef(static_cast<jobject>(t))));" + newLine);
            output.write("    ::jace::detach();" + newLine);
            output.write("    return" + returnValue + ";" + newLine);
            output.write("  }" + newLine);
            output.write("  catch (std::exception& e)" + newLine);
            output.write("  {" + newLine);
            output.write("    std::string msg = std::string(\"An unexpected JNI error has occurred: \") + e.what();" + newLine);
            output.write("    jace::proxy::java::lang::RuntimeException ex(jace::java_new<jace::proxy::java::lang::RuntimeException>(msg));" + newLine);
            output.write("    env->Throw(static_cast<jthrowable>(env->NewLocalRef(static_cast<jobject>(ex))));" + newLine);
            output.write("    ::jace::detach();" + newLine);
            output.write("    return" + returnValue + ";" + newLine);
            output.write("  }" + newLine);
            output.write("}" + newLine);
            output.write(newLine);
        }
    }
