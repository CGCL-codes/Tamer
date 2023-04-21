    final FileChannel getFileChannel0() throws IOException
    {
      if (this.closed)
        throw new ClosedChannelException();

      if (this.fileChannel != null)
        return this.fileChannel;

      final RandomAccessFile f = new RandomAccessFile(this.file, "r");
      try
      {
        final FileChannel fc = f.getChannel();
        lockFile(fc);
        this.fileChannel = fc;
        return fc;
      }
      catch (final Throwable ex)
      {
        IO.tryClose(f);
        throw Exceptions.rethrow(ex);
      }
    }
