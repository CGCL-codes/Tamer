    public void write() {
        if (backup) makeBackup();
        FileOutputStream outStream;
        try {
            outStream = new FileOutputStream(root.getCanonicalPath() + "/" + FILE_NAME);
            FileChannel out = outStream.getChannel();
            out.position(512);
            header.setOffset_files((int) out.position());
            out.write(files.toByteBuffer());
            out.position(out.position() + (512 - out.position() % 512));
            header.setOffset_lists((int) out.position());
            out.write(lists.toByteBuffer());
            out.position(out.position() + (512 - out.position() % 512));
            header.setOffset_list_entries((int) out.position());
            header.setSearch_list(lists.getSearchListId());
            out.write(listentries.toByteBuffer());
            out.position(out.position() + (512 - out.position() % 512));
            header.setOffset_paths((int) out.position());
            out.write(paths.toByteBuffer());
            out.position(out.position() + (512 - out.position() % 512));
            header.setOffset_strings((int) out.position());
            out.write(strings.toByteBuffer());
            out.position(out.position() + (512 - out.position() % 512));
            header.setOffset_private_data((int) out.position());
            out.position(0);
            out.write(header.toByteBuffer());
            out.position(502);
            out.close();
            outStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
