    private void writeTopics(File file) {
        if (_newfile) {
            File lastpath = new File(_preferences.getString("lastpath", "."));
            JFileChooser chooser = new JFileChooser(lastpath);
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Help files", "xnhlp");
            chooser.setFileFilter(filter);
            int returnVal = chooser.showSaveDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                lastpath = chooser.getCurrentDirectory();
                _preferences.put("lastpath", lastpath.getAbsolutePath());
                file = chooser.getSelectedFile();
                String fl = file.getAbsolutePath();
                if (!fl.endsWith(".xnhlp")) {
                    file = new File(fl + ".xnhlp");
                }
            } else {
                return;
            }
        }
        String jarName = file.getAbsolutePath();
        try {
            FileOutputStream fout = new FileOutputStream(jarName);
            JarOutputStream jout = new JarOutputStream(fout);
            {
                ZipEntry ze = new ZipEntry("HelpTopics.xnhlp");
                jout.putNextEntry(ze);
                writeObject(jout, _topics);
            }
            {
                @SuppressWarnings("unchecked") Enumeration<DefaultMutableTreeNode> en = _topics.breadthFirstEnumeration();
                while (en.hasMoreElements()) {
                    DefaultMutableTreeNode topic = en.nextElement();
                    HelpTopic ht = (HelpTopic) topic.getUserObject();
                    HelpEntry he = (HelpEntry) _help.get(ht.getTopicId());
                    ZipEntry hze = new ZipEntry(ht.getTopicId() + ".top");
                    jout.putNextEntry(hze);
                    writeObject(jout, he);
                }
            }
            {
                Set<String> skeys = _images.keySet();
                Vector<String> keys = new Vector<String>();
                Iterator<String> it = skeys.iterator();
                while (it.hasNext()) {
                    String id = it.next();
                    XMLNoteImageIcon icn = _images.get(id);
                    if (icn.type() == XMLNoteImageIcon.Type.IMAGEICON) {
                        keys.add(id);
                    }
                }
                ZipEntry kze = new ZipEntry("ImageIds.vec");
                jout.putNextEntry(kze);
                writeObject(jout, keys);
                it = keys.iterator();
                while (it.hasNext()) {
                    XMLNoteImageIcon img = _images.get(it.next());
                    HelpImage himg = new HelpImage(img.getId(), img.getOriginal(), img.getDescription(), img.getSize());
                    ZipEntry ize = new ZipEntry(img.getId() + ".img");
                    jout.putNextEntry(ize);
                    himg.write(jout);
                }
            }
            jout.close();
            fout.close();
            _helpjar = new File(jarName);
            setTitle();
            _saved = true;
            _newfile = false;
        } catch (IOException e) {
            DefaultXMLNoteErrorHandler.exception(e);
        }
    }
