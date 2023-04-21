  @Override
  public final void execute() throws BuildException
  {
    if (this.tarFile == null)
      throw new BuildException("tarfile attribute must be set!", getLocation());
    if (this.tarFile.exists() && this.tarFile.isDirectory())
      throw new BuildException("tarfile is a directory!", getLocation());
    if (this.tarFile.exists() && !this.tarFile.canWrite())
      throw new BuildException("Can not write to the specified tarfile!", getLocation());

    final List<TarFileSet> savedFileSets = new ArrayList<>(this.filesets);
    try
    {
      if (this.baseDir != null)
      {
        if (!this.baseDir.exists())
          throw new BuildException("basedir does not exist!", getLocation());

        // add the main fileset to the list of filesets to process.
        final TarFileSet mainFileSet = new TarFileSet(this.fileset);
        mainFileSet.setDir(this.baseDir);
        this.filesets.add(mainFileSet);
      }

      if (this.filesets.isEmpty())
      {
        throw new BuildException(
          "You must supply either a basedir attribute or some nested filesets.",
          getLocation()
        );
      }

      final Project project = getProject();
      LinkedHashMap<FileEntry,TarFileSet> files = null; // lazily initialized

      // check if tar is out of date with respect to each fileset
      boolean upToDate = this.tarFile.isFile();
      for (final TarFileSet fs : this.filesets)
      {
        final String[] fileNames = fs.getFiles(project);
        if ((fileNames.length > 1) && (fs.getFullpath().length() > 0))
        {
          throw new BuildException(
            "fullpath attribute may only be specified for filesets that specify a single file."
          );
        }
        Arrays.sort(fileNames); // sort files for better performance when synchronizing .tar via RSync
        final File dir = fs.getDir(project);
        if (upToDate && !archiveIsUpToDate(fileNames, dir))
          upToDate = false;

        for (final String fileName : fileNames)
        {
          if (files == null)
            files = new LinkedHashMap<>(Math.max(1024, fileNames.length * 2));

          final FileEntry f = new FileEntry(fileName, new File(dir, fileName));
          if (files.put(f, fs) == null)
          {
            if (this.tarFile.equals(f.file))
              throw new BuildException("A tar file cannot include itself", getLocation());
          }
        }
      }

      if (upToDate)
      {
        log("Nothing to do: " + tarFile.getAbsolutePath() + " is up to date.", Project.MSG_INFO);
        return;
      }

      log("Building tar: " + tarFile.getAbsolutePath(), Project.MSG_INFO);

      FileOutputStream fOut = null;
      TarOutputStream tOut = null;
      try
      {
        fOut = new FileOutputStream(this.tarFile);
        final FileLock lock = fOut.getChannel().tryLock();
        if (lock == null)
          throw new BuildException("destination file is write locked: " + this.tarFile);
        tOut = new TarOutputStream(this.compression.createOutputStream(fOut));
        final boolean compressed = !CompressionAttribute.NONE.equals(this.compression.getValue());
        //tOut.setDebug(true);
        //tOut.setLongFileMode(TarOutputStream.LONGFILE_GNU);

        if ((files != null) && !files.isEmpty())
        {
          this.longWarningGiven = false;
          for (
            final Iterator<Map.Entry<FileEntry,TarFileSet>> it = files.entrySet().iterator();
            it.hasNext();
          )
          {
            final Map.Entry<FileEntry,TarFileSet> e = it.next();
            final FileEntry f = e.getKey();
            final TarFileSet fs = e.getValue();
            it.remove();
            tarFile(f.file, tOut, f.childName, fs);
          }
          files = null;
        }

        tOut.close();
        tOut = null;
        fOut = null;
      }
      catch (final IOException ex)
      {
        throw new BuildException("Problem creating TAR: " + ex.getMessage(), ex, getLocation());
      }
      finally
      {
        FileUtils.close(tOut);
        FileUtils.close(fOut);
      }
    }
    finally
    {
      this.filesets = savedFileSets;
    }
  }
