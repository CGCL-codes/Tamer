    public void read(final String propertiesSetupResourceName, final ClassLoaderProvider classLoaderProvider, final FilePersistenceProperties filePersistenceProperties) throws FilePersistenceException {
        try {
            final URL url = classLoaderProvider.getResource(propertiesSetupResourceName);
            final InputStream inputStream = url.openStream();
            final Properties properties = new Properties();
            properties.load(inputStream);
            final Set<Entry<Object, Object>> entrySet = properties.entrySet();
            for (Entry<Object, Object> entry : entrySet) {
                final String key = (String) entry.getKey();
                final String value = (String) entry.getValue();
                if (JOAFIP_PATH.equals(key)) {
                    filePersistenceProperties.setPathName(value);
                } else if (JOAFIP_DATA_MODEL_IDENTIFIER.equals(key)) {
                    filePersistenceProperties.setDataModelIdentifier(intValue(key, value));
                } else if (JOAFIP_DATA_MODEL_CONVERSION_DEF_FILE.equals(key)) {
                    final URL conversionDefUrl = classLoaderProvider.getResource(value);
                    final InputStream conversionDefInputStream = conversionDefUrl.openStream();
                    final InputStreamAndSource fileInputStreamAndSource = new InputStreamAndSource(conversionDefInputStream, value);
                    filePersistenceProperties.setDataModelConversionDefInputStream(fileInputStreamAndSource);
                } else if (JOAFIP_GARBAGE_MANAGEMENT.equals(key)) {
                    filePersistenceProperties.setGarbageManagement(isEnabled(key, value));
                } else if (JOAFIP_PROXY_MODE.equals(key)) {
                    filePersistenceProperties.setProxyMode(isEnabled(key, value));
                } else if (JOAFIP_CRASH_SAFE_MODE.equals(key)) {
                    filePersistenceProperties.setCrashSafeMode(isEnabled(key, value));
                } else if (JOAFIP_FILE_CACHE.equals(key)) {
                    filePersistenceProperties.setUseCacheMode(isEnabled(key, value));
                } else if (JOAFIP_FILE_CACHE_PAGE_SIZE.equals(key)) {
                    filePersistenceProperties.setPageSize(intValue(key, value));
                } else if (JOAFIP_FILE_CACHE_MAX_PAGE.equals(key)) {
                    filePersistenceProperties.setMaxPage(intValue(key, value));
                } else if (JOAFIP_ZIP_COMPRESSION_LEVEL.equals(key)) {
                    filePersistenceProperties.setZipCompressionLevelSetted(true);
                    filePersistenceProperties.setZipCompressionLevel(intValue(key, value));
                } else if (JOAFIP_SUBSTITUTION_OF_JAVA_UTIL_COLLECTION.equals(key)) {
                    filePersistenceProperties.setSubsOfJavaUtil(isEnabled(key, value));
                } else if (key.startsWith(JOAFIP_SUBSTITUTE)) {
                    addSubtitution(filePersistenceProperties, key.substring(JOAFIP_SUBSTITUTE.length()), value);
                } else if (key.startsWith(JOAFIP_STORE_MODE)) {
                    addStoreMode(filePersistenceProperties, key.substring(JOAFIP_STORE_MODE.length()), value);
                } else if (key.startsWith(JOAFIP_NOT_STORABLE)) {
                    addNotStorable(filePersistenceProperties, key.substring(JOAFIP_NOT_STORABLE.length()), isEnabled(key, value));
                } else if (key.startsWith(JOAFIP_DEPRECATED_IN_STORE)) {
                    addDeprecatedInStore(filePersistenceProperties, key.substring(JOAFIP_DEPRECATED_IN_STORE.length()), isEnabled(key, value));
                } else if (key.equals(JOAFIP_STORE_ONLY_MARKED_STORABLE)) {
                    filePersistenceProperties.setStoreOnlyMarkedStorable(isEnabled(key, value));
                } else if (key.equals(JOAFIP_RECORD_SAVE_ACTIONS)) {
                    filePersistenceProperties.setRecordSaveActions(isEnabled(key, value));
                } else if (key.startsWith(JOAFIP_FORCE_ENHANCE)) {
                    addForceEnhance(filePersistenceProperties, key.substring(JOAFIP_FORCE_ENHANCE.length()), isEnabled(key, value));
                } else if (key.startsWith(JOAFIP_OBJECT_I_O)) {
                    final String[] split = value.split(";");
                    if (split.length != 2) {
                        throw new FilePersistenceException("must define two class name " + key + "=" + value);
                    }
                    addObjectIo(filePersistenceProperties, key.substring(JOAFIP_OBJECT_I_O.length()), split[0], split[1]);
                } else if (key.startsWith(JOAFIP_STORED_ENUM)) {
                    addStoredMutableEnum(filePersistenceProperties, key.substring(JOAFIP_STORED_ENUM.length()), isEnabled(key, value));
                } else if (key.startsWith(JOAFIP_STORED_MUTABLE_ENUM)) {
                    addStoredMutableEnum(filePersistenceProperties, key.substring(JOAFIP_STORED_MUTABLE_ENUM.length()), isEnabled(key, value));
                } else if (key.startsWith(JOAFIP_STORED_IMMUTABLE_ENUM)) {
                    addStoredImmutableEnum(filePersistenceProperties, key.substring(JOAFIP_STORED_IMMUTABLE_ENUM.length()), isEnabled(key, value));
                } else if (JOAFIP_BACKGROUND_GARBAGE_SWEEP.equals(key)) {
                    final boolean backgroundGarbageSweepEnabled = true ^ DISABLED.equals(value);
                    filePersistenceProperties.setBackgroundGarbageSweepEnabled(backgroundGarbageSweepEnabled);
                    if (backgroundGarbageSweepEnabled) {
                        filePersistenceProperties.setBackgroundGarbageSweepSleepTime(intValue(key, value));
                    }
                } else if (key.equals(JOAFIP_MAINTENED_IN_MEMORY)) {
                    filePersistenceProperties.setMaintenedInMemory(isEnabled(key, value));
                } else if (key.equals(JOAFIP_MAINTENED_IN_MEMORY_QUOTA)) {
                    filePersistenceProperties.setMaintenedInMemoryQuota(intValue(key, value));
                } else if (key.equals(JOAFIP_AUTO_SAVE)) {
                    filePersistenceProperties.setAutoSaveEnabled(isEnabled(key, value));
                } else if (key.equals(JOAFIP_MAX_IN_MEMORY_THRESHOLD)) {
                    filePersistenceProperties.setMaxInMemoryThreshold(intValue(key, value));
                } else if (key.equals(JOAFIP_DATA_FILE_NAME)) {
                    filePersistenceProperties.setDataFileName(value);
                } else if (key.equals(JOAFIP_BACKUP_DATA_FILE_NAME)) {
                    filePersistenceProperties.setBackupDataFileName(value);
                } else if (key.equals(JOAFIP_STATE_OK_FLAG_FILE_NAME)) {
                    filePersistenceProperties.setStateOkFlagFileName(value);
                } else if (key.equals(JOAFIP_STATE_BACKUP_OK_FLAG_FILE_NAME)) {
                    filePersistenceProperties.setStateBackupOkFlagFileName(value);
                } else if (key.equals(JOAFIP_GLOBAL_STATE_FLAG_FILE_NAME)) {
                    filePersistenceProperties.setGlobalStateFlagFileName(value);
                } else if (key.equals(JOAFIP_MAX_FILE_OPERATION_RETRY)) {
                    filePersistenceProperties.setMaxFileOperationRetry(intValue(key, value));
                } else if (key.equals(JOAFIP_FILE_OPERATION_RETRY_MS_DELAY)) {
                    filePersistenceProperties.setFileOperationRetryMsDelay(intValue(key, value));
                } else if (key.equals(JOAFIP_NO_MORE_DATA_ACTION)) {
                    final EnumNoMoreDataAction noMoreDataAction;
                    if ("delete".equals(value)) {
                        noMoreDataAction = EnumNoMoreDataAction.DELETE_FILE;
                    } else if ("resize".equals(value)) {
                        noMoreDataAction = EnumNoMoreDataAction.RESIZE_FILE;
                    } else if ("rename".equals(value)) {
                        noMoreDataAction = EnumNoMoreDataAction.RENAME_FILE;
                    } else if ("preserve".equals(value)) {
                        noMoreDataAction = EnumNoMoreDataAction.PRESERVE_FILE;
                    } else {
                        throw new FilePersistenceException("on of 'delete','resize','rename','preserve' value expected for key " + key);
                    }
                    filePersistenceProperties.setNoMoreDataAction(noMoreDataAction);
                }
            }
        } catch (MalformedURLException exception) {
            throw new FilePersistenceException(exception);
        } catch (IOException exception) {
            throw new FilePersistenceException(exception);
        } catch (URISyntaxException exception) {
            throw new FilePersistenceException(exception);
        }
    }
