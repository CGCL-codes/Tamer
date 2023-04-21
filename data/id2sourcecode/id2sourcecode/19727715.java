  private void tarFile(
    final File            file,
    final TarOutputStream tOut,
          String          vPath,
    final TarFileSet      tarFileSet
  )
  throws IOException
  {
    final String fullpath = tarFileSet.getFullpath();
    if (fullpath.length() > 0)
      vPath = fullpath;
    else if (vPath.length() <= 0)
      return; // don't add "" to the archive
    else
    {
      if (file.isDirectory() && !vPath.endsWith("/"))
        vPath += "/";

      final String prefix = tarFileSet.getPrefix();
      // '/' is appended for compatibility with the zip task.
      if ((prefix.length() > 0) && !prefix.endsWith("/"))
        vPath = prefix + "/" + vPath;
      else
        vPath = prefix + vPath;
    }

    while (vPath.startsWith("/") && !tarFileSet.getPreserveLeadingSlashes())
    {
      if (vPath.length() <= 1)
        return; // we would end up adding "" to the archive
      vPath = vPath.substring(1);
    }

    FileInputStream fIn = null;
    try
    {
      if (vPath.length() > TarEntry.SHORT_NAME_LENGTH)
      {
        if (this.longFileMode.isWarnMode())
        {
          log(
            "Entry: " + vPath + " longer than " + TarEntry.SHORT_NAME_LENGTH + " characters.",
            Project.MSG_WARN
          );
          if (!this.longWarningGiven)
          {
            log(
              "Resulting tar file can only be processed successfully by GNU compatible tar commands",
              Project.MSG_WARN
            );
            this.longWarningGiven = true;
          }
        }
        else if (this.longFileMode.isFailMode())
        {
          throw new BuildException(
            "Entry: " + vPath + " longer than " + TarEntry.SHORT_NAME_LENGTH + "characters.",
            getLocation()
          );
        }
      }

      final TarEntry te;
      if (file.isDirectory())
      {
        te = new TarEntry(TarEntryType.DIRECTORY);
        te.setPermissions(tarFileSet.dirMode);
      }
      else
      {
        te = new TarEntry(TarEntryType.NORMAL);
        if (file.canExecute())
        {
          te.setPermissions(UnixFilePermissions.valueOf(
            tarFileSet.fileMode.flags | UnixFilePermissions.OWNER_EXEC
          ));
        }
        else
        {
          te.setPermissions(tarFileSet.fileMode);
        }
        if (file.length() <= 0)
        {
          te.setSize(0);
        }
        else
        {
          fIn = new FileInputStream(file);
          final FileChannel inChannel = fIn.getChannel();
          if (inChannel.tryLock(0, te.getSize(), true) == null)
            throw new BuildException("input file is locked exclusively by another process: " + file);
          te.setSize(inChannel.size());
        }
      }
      te.setModTime(file.lastModified());
      te.setGroupId(tarFileSet.getGid());
      te.setGroupName(tarFileSet.getGroup());
      te.setUserId(tarFileSet.getUid());
      te.setUserName(tarFileSet.getUserName());
      te.setPath(vPath);
      tOut.openEntry(te);

      if (fIn != null)
      {
        if (tOut.transferFrom(fIn, te.getSize()) != te.getSize())
          throw new IOException(); // should not happen

        fIn.close();
        fIn = null;
      }

      tOut.closeEntry();
    }
    finally
    {
      IO.tryClose(fIn);
    }
  }
