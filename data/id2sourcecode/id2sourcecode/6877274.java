    private void generateForPluginDescriptor(final File baseDir, final PluginDescriptor descr) throws Exception {
        File destDir = new File(baseDir, descr.getId());
        destDir.mkdirs();
        File srcDocsFolder = IoUtil.url2file(pathResolver.resolvePath(descr, descr.getDocsPath()));
        if ((srcDocsFolder != null) && srcDocsFolder.isDirectory()) {
            File destDocsFolder = new File(destDir, "extra");
            destDocsFolder.mkdir();
            IoUtil.copyFolder(srcDocsFolder, destDocsFolder, true);
        }
        List<PluginDescriptor> dependedPlugins = new LinkedList<PluginDescriptor>();
        for (PluginDescriptor dependedDescr : registry.getPluginDescriptors()) {
            if (dependedDescr.getId().equals(descr.getId())) {
                continue;
            }
            for (PluginPrerequisite pre : dependedDescr.getPrerequisites()) {
                if (pre.getPluginId().equals(descr.getId()) && pre.matches()) {
                    dependedPlugins.add(dependedDescr);
                    break;
                }
            }
        }
        Map<String, Object> ctx = createConext(1);
        ctx.put("descriptor", descr);
        ctx.put("dependedPlugins", dependedPlugins);
        processTemplateFile(ctx, "plugin.jxp", new File(destDir, "index.html"));
        for (PluginFragment fragment : descr.getFragments()) {
            generateForPluginFragment(baseDir, fragment);
        }
        if (!descr.getExtensionPoints().isEmpty()) {
            File extPointsDir = new File(destDir, "extp");
            extPointsDir.mkdir();
            for (ExtensionPoint extPoint : descr.getExtensionPoints()) {
                ctx = createConext(3);
                ctx.put("extPoint", extPoint);
                File dir = new File(extPointsDir, extPoint.getId());
                dir.mkdir();
                processTemplateFile(ctx, "extpoint.jxp", new File(dir, "index.html"));
            }
        }
        if (!descr.getExtensions().isEmpty()) {
            File extsDir = new File(destDir, "ext");
            extsDir.mkdir();
            for (Extension ext : descr.getExtensions()) {
                ctx = createConext(3);
                ctx.put("ext", ext);
                File dir = new File(extsDir, ext.getId());
                dir.mkdir();
                processTemplateFile(ctx, "ext.jxp", new File(dir, "index.html"));
            }
        }
    }
