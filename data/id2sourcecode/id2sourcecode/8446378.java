    private CopyOnWriteMap getAttributeMapForType(int event_type) {
        return event_type == DownloadPropertyEvent.PT_TORRENT_ATTRIBUTE_WILL_BE_READ ? read_attribute_listeners_map_cow : write_attribute_listeners_map_cow;
    }
