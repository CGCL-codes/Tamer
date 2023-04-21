  @SuppressWarnings("finally")
  public DenseFileCache(
    CacheSystem cacheSystem, String name, DenseFileCacheProperties properties, ClassLoader classloader
  )
  throws IOException
  {
    super(cacheSystem, name, properties);

    properties = (DenseFileCacheProperties) super.properties;

    this.classloader = classloader;
    this.properties  = properties;
    File path        = properties.getPath();

    if (path == null)
    {
      path = Files.createTempFile(name, ".cache").toFile();
      path.deleteOnExit();
      this.deleteOnClose = true;
    }
    else if (path.isDirectory())
    {
      path = Files.createTempFile(path.toPath(), name, ".cache").toFile();
      path.deleteOnExit();
      this.deleteOnClose = true;
    }
    else if (path.isFile())
    {
      this.deleteOnClose = properties.deleteFileOnClose;
    }
    else if (properties.deleteFileOnClose)
    {
      Files.mkdirs(path);
      path = Files.createTempFile(path.toPath(), name, ".cache").toFile();
      path.deleteOnExit();
      this.deleteOnClose = true;
    }
    else
    {
      final File parent = path.getParentFile();
      if (parent != null)
        Files.mkdirs(parent);
      path.createNewFile();
      this.deleteOnClose = false;
    }

    this.path = path;

    this.map = new ConcurrentHashXMap<>(1);
    final RandomAccessFile file = Files.openLockedRandomAccessFile(path, "rw", 0, TimeUnit.MILLISECONDS);
    this.file = file;

    final long existingFileLength = this.file.length();
    if (existingFileLength != 0)
    {
      file.seek(0);

      if
      (
           (existingFileLength < FILE_HEADER_LENGTH)
        || (file.readInt() != MAGIC)
      )
      {
        try
        {
          file.close();
        }
        finally
        {
          throw new IOException(
            "File was existing and either is corrupted or has not bean created by this class:\n" + path
          );
        }
      }
      else if (properties.persistent && (file.readInt() == VERSION))
      {
        final int flags     = file.read();
        final long vmLockId = file.readLong();

        if (vmLockId == VM_LOCK_ID)
        {
          try
          {
            file.close();
          }
          finally
          {
            throw new IOException("File already is in use by another thread:\n" + path);
          }
        }
        else if ((flags & FLAG_CLEAN_SHUTDOWN) != 0) // clean shutdown?
        {
          if (((flags & FLAG_COMPRESSED) != 0) == (properties.compressionLevel > 0))
          {
            // TODO: read persistent entries into map
          }
        }
        else
        {
          // log a warning
        }
      }
    }

    file.seek(0);
    file.writeInt(MAGIC);
    file.writeInt(VERSION);

    // flags - clear clean shutdown flag
    int flags = 0;
    if (properties.compressionLevel > 0)
      flags |= FLAG_COMPRESSED;
    file.writeInt(flags);

    // set lock id (clear on clean shutdown)
    file.writeLong(VM_LOCK_ID);

    this.fileLength = (int) file.getFilePointer();
    assert (this.fileLength == FILE_HEADER_LENGTH);
    this.fileChannel = file.getChannel();

    this.fileOut = new GrowingFileBufferOutputStream();
    this.fileOut.position(this.fileLength);

    if (properties.compressionLevel <= 0)
    {
      this.out = fileOut;
    }
    else
    {
      this.out = new DenseFileCache.NonClosingBufferedOutputStream(
        new DenseFileCache.NonClosingDeflatedOutputStream(this.fileOut, properties.compressionLevel)
      );
    }

    this.cacheStatistics  = new DefaultCacheStatistics();
    this.lock             = new ReentrantReadWriteLock(properties.fair);
    this.readLock         = this.lock.readLock();
    this.writeLock        = this.lock.writeLock();
  }
