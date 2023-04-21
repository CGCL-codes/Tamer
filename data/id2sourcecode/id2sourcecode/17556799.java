  private void purgeFile(final boolean force) throws IOException
  {
    if (!force)
    {
      final int deletedLength = this.deletedLength;
      if (deletedLength == 0)
      {
        return;
      }
      else
      {
        final int maxFileOverhead = this.properties.maxFileOverhead;
        if ((maxFileOverhead == -1) || (ensureFileOpen().length() / this.fileLength <= maxFileOverhead))
          return;
      }
    }

    boolean ok = false;
    beginWrite();

    try
    {
      if (this.deletedLength == 0)
        return;

      final Entry[] entries = new Entry[map.size()];

      // copy entries to array, oldest first
      {
        int i = 0;
        for (Entry e = this.head.prev; e != this.head; e = e.prev)
          entries[i++] = e;
      }

      // Sort ascending by offset.
      // The array should already be mostly sorted, thus this goes quick
      TunedQuickSort.getInstance().apply(entries);

      final MappedByteBuffer writeBuffer = fileBuffer();
      final ByteBuffer       readBuffer  = writeBuffer.duplicate();

      writeBuffer.position(FILE_HEADER_LENGTH);

      for (int i = 0; i < entries.length; i++)
      {
        Entry<?,?> entry = entries[i];
        entries[i] = null;

        final int srcPos = entry.offset;
        final int dstPos = writeBuffer.position();

        if (srcPos != dstPos)
        {
          entry.offset = dstPos;
          int srcLimit = srcPos + ENTRY_HEADER_LENGTH + entry.length;

          while (++i < entries.length)
          {
            entry = entries[i];
            if (entry.offset == srcLimit)
            {
              entry.offset = dstPos + (srcLimit - srcPos);
              srcLimit += ENTRY_HEADER_LENGTH + entry.length;
              entries[i] = null;
            }
            else
            {
              i--;
              break;
            }
          }

          readBuffer.limit(srcLimit);
          readBuffer.position(srcPos);
          writeBuffer.put(readBuffer);
        }
      }

      this.deletedLength  = 0;

      final int spaceUsed = writeBuffer.position();
      this.fileLength     = spaceUsed;

      final int maxFileSize = Math.max(
        (int) (spaceUsed + (spaceUsed * this.properties.fileGrowFactor)),
        Math.max(this.properties.minimumFileSize, spaceUsed * this.properties.maxFileOverhead)
      );

      if (writeBuffer.capacity() > maxFileSize)
      {
        this.fileBuffer = null;
        ensureFileOpen().setLength(maxFileSize);
      }
      else
      {
        writeBuffer.limit(writeBuffer.capacity());
        writeBuffer.position(0);
      }

      ok = true;
    }
    finally
    {
      if (!ok)
      {
        this.fileBuffer = null;
        try
        {
          clear();
        }
        catch (final Throwable ex)
        {
          // ignore, throw previous exception
        }
      }
      endWrite();
    }
  }
