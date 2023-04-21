  private void restoreFile(final TarInputStream in, final TarEntry tarEntry, final File destFile)
  throws IOException
  {
    try
    {
      final File parentDir = destFile.getParentFile();
      if (parentDir != null)
        parentDir.mkdirs();

      final long size = tarEntry.getSize();
      RandomAccessFile out = new RandomAccessFile(destFile, "rw");
      try
      {
        if (size > 0)
        {
          final FileChannel fileChannel = out.getChannel();
          if (fileChannel.tryLock(0, Long.MAX_VALUE, false) == null)
            throw new IOException("file is locked: " + destFile);
          if (size > 512)
            out.setLength(size);
          if (in.transferToByteChannel(fileChannel, size) != size)
            throw new IOException();
        }
        out.close();
        out = null;
      }
      finally
      {
        IO.tryClose(out);
      }
      destFile.setLastModified(tarEntry.getTimeLastModified());
      log(tarEntry.getPath() + " => " + destFile, this.verbose);
    }
    catch (final FileNotFoundException ex)
    {
      log("Unable to restore file " + destFile.getPath(), Project.MSG_WARN);
    }
  }
