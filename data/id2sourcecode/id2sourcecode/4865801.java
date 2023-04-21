    @SuppressWarnings("finally")
    public synchronized void commit() throws IOException
    {
      final SwapBuffer owner = this.owner;
      if (owner == null)
        throw new ClosedChannelException();

      FileOutputStream out = (FileOutputStream) getOut();

      if (out != null)
      {
        final File file = this.file;
        try
        {
          flush();
          FileChannel fc = out.getChannel();
          final long size = fc.size();
          fc.force(true);
          fc = null;
          out = null;

          this.file = null;
          close();

          owner.update(new ContentInFile(owner, file, size, file.lastModified()));
        }
        catch (final IOException ex)
        {
          this.file = file;
          try
          {
            close();
          }
          finally
          {
            throw ex;
          }
        }
        finally
        {
          this.owner = null;
        }
      }
      else
      {
        this.owner = null;
        ByteBuffer memoryBuffer = getBuffer();
        close();
        memoryBuffer.flip();

        if (memoryBuffer.remaining() != memoryBuffer.capacity())
        {
          ByteBuffer newBuffer = ByteBuffer.allocate(memoryBuffer.remaining());
          newBuffer.put(memoryBuffer);
          memoryBuffer = newBuffer;
          newBuffer.flip();
        }

        owner.update(new ContentInMemory(owner, System.currentTimeMillis(), memoryBuffer.duplicate()));
      }
    }
