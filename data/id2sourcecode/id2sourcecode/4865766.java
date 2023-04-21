    final FileInputStream getFileInputStream() throws IOException
    {
      if (this.closed)
        throw new ClosedChannelException();

      final FileInputStream f = new FileInputStream(this.file);
      try
      {
        lockFile(f.getChannel());
        return f;
      }
      catch (final Throwable ex)
      {
        IO.tryClose(f);
        throw Exceptions.rethrow(ex);
      }
    }
