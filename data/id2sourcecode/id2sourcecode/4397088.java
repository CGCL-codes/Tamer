    public synchronized String putMismatch(Variant variant) {
        if (persistenceMethod == PostgreSQLStore.OID || persistenceMethod == PostgreSQLStore.BYTEA) {
            currId = putModel("feature", "feature_tag", "variant", mtb, variant);
            return (currId.toString());
        } else if (persistenceMethod == PostgreSQLStore.FIELDS) {
            try {
                StringBuffer sb = new StringBuffer();
                boolean isFirst = true;
                for (String key : variant.getTags().keySet()) {
                    if (isFirst) {
                        isFirst = false;
                    } else {
                        sb.append(":");
                    }
                    String value = variant.getTagValue(key);
                    if (value != null && !"".equals(value)) {
                        sb.append(key + "=" + value);
                    } else {
                        sb.append(key);
                    }
                }
                variant.setKeyvalues(sb.toString());
                if (variant.getId() != null && !"".equals(variant.getId())) {
                    psVariantUpdate.setInt(1, variant.getType());
                    psVariantUpdate.setString(2, variant.getContig());
                    psVariantUpdate.setInt(3, variant.getStartPosition());
                    psVariantUpdate.setInt(4, variant.getStopPosition());
                    psVariantUpdate.setInt(5, variant.getFuzzyStartPositionMax());
                    psVariantUpdate.setInt(6, variant.getFuzzyStopPositionMin());
                    psVariantUpdate.setString(7, variant.getReferenceBase());
                    psVariantUpdate.setString(8, variant.getConsensusBase());
                    psVariantUpdate.setString(9, variant.getCalledBase());
                    psVariantUpdate.setFloat(10, variant.getReferenceCallQuality());
                    psVariantUpdate.setFloat(11, variant.getConsensusCallQuality());
                    psVariantUpdate.setFloat(12, variant.getMaximumMappingQuality());
                    psVariantUpdate.setInt(13, variant.getReadCount());
                    psVariantUpdate.setString(14, variant.getReadBases());
                    psVariantUpdate.setString(15, variant.getBaseQualities());
                    psVariantUpdate.setInt(16, variant.getCalledBaseCount());
                    psVariantUpdate.setInt(17, variant.getCalledBaseCountForward());
                    psVariantUpdate.setInt(18, variant.getCalledBaseCountReverse());
                    psVariantUpdate.setInt(19, variant.getZygosity());
                    psVariantUpdate.setFloat(20, variant.getReferenceMaxSeqQuality());
                    psVariantUpdate.setFloat(21, variant.getReferenceAveSeqQuality());
                    psVariantUpdate.setFloat(22, variant.getConsensusMaxSeqQuality());
                    psVariantUpdate.setFloat(23, variant.getConsensusAveSeqQuality());
                    psVariantUpdate.setString(24, variant.getCallOne());
                    psVariantUpdate.setString(25, variant.getCallTwo());
                    psVariantUpdate.setInt(26, variant.getReadsSupportingCallOne());
                    psVariantUpdate.setInt(27, variant.getReadsSupportingCallTwo());
                    psVariantUpdate.setInt(28, variant.getReadsSupportingCallThree());
                    psVariantUpdate.setInt(29, variant.getSvType());
                    psVariantUpdate.setInt(30, variant.getRelativeLocation());
                    psVariantUpdate.setInt(31, variant.getTranslocationType());
                    psVariantUpdate.setString(32, variant.getTranslocationDestinationContig());
                    psVariantUpdate.setInt(33, variant.getTranslocationDestinationStartPosition());
                    psVariantUpdate.setInt(34, variant.getTranslocationDestinationStopPosition());
                    psVariantUpdate.setString(35, variant.getKeyvalues());
                    psVariantUpdate.setInt(36, Integer.parseInt(variant.getId()));
                    psVariantUpdate.executeUpdate();
                } else {
                    psVariant.setInt(1, variant.getType());
                    psVariant.setString(2, variant.getContig());
                    psVariant.setInt(3, variant.getStartPosition());
                    psVariant.setInt(4, variant.getStopPosition());
                    psVariant.setInt(5, variant.getFuzzyStartPositionMax());
                    psVariant.setInt(6, variant.getFuzzyStopPositionMin());
                    psVariant.setString(7, variant.getReferenceBase());
                    psVariant.setString(8, variant.getConsensusBase());
                    psVariant.setString(9, variant.getCalledBase());
                    psVariant.setFloat(10, variant.getReferenceCallQuality());
                    psVariant.setFloat(11, variant.getConsensusCallQuality());
                    psVariant.setFloat(12, variant.getMaximumMappingQuality());
                    psVariant.setInt(13, variant.getReadCount());
                    psVariant.setString(14, variant.getReadBases());
                    psVariant.setString(15, variant.getBaseQualities());
                    psVariant.setInt(16, variant.getCalledBaseCount());
                    psVariant.setInt(17, variant.getCalledBaseCountForward());
                    psVariant.setInt(18, variant.getCalledBaseCountReverse());
                    psVariant.setInt(19, variant.getZygosity());
                    psVariant.setFloat(20, variant.getReferenceMaxSeqQuality());
                    psVariant.setFloat(21, variant.getReferenceAveSeqQuality());
                    psVariant.setFloat(22, variant.getConsensusMaxSeqQuality());
                    psVariant.setFloat(23, variant.getConsensusAveSeqQuality());
                    psVariant.setString(24, variant.getCallOne());
                    psVariant.setString(25, variant.getCallTwo());
                    psVariant.setInt(26, variant.getReadsSupportingCallOne());
                    psVariant.setInt(27, variant.getReadsSupportingCallTwo());
                    psVariant.setInt(28, variant.getReadsSupportingCallThree());
                    psVariant.setInt(29, variant.getSvType());
                    psVariant.setInt(30, variant.getRelativeLocation());
                    psVariant.setInt(31, variant.getTranslocationType());
                    psVariant.setString(32, variant.getTranslocationDestinationContig());
                    psVariant.setInt(33, variant.getTranslocationDestinationStartPosition());
                    psVariant.setInt(34, variant.getTranslocationDestinationStopPosition());
                    psVariant.setString(35, variant.getKeyvalues());
                    psVariant.executeUpdate();
                    if (this.getSettings().isReturnIds()) {
                        ResultSet rs = null;
                        rs = psVariantId.executeQuery();
                        if (rs != null) {
                            if (rs.next()) {
                                variant.setId(new Integer(rs.getInt(1)).toString());
                            }
                        }
                        rs.close();
                    }
                }
                addTags(variant, "variant");
            } catch (SQLException e) {
                try {
                    conn.rollback();
                } catch (SQLException e2) {
                    e2.printStackTrace();
                }
                e.printStackTrace();
                System.err.println(e.getMessage());
            } catch (Exception e) {
                try {
                    conn.rollback();
                } catch (SQLException e2) {
                    e2.printStackTrace();
                }
                e.printStackTrace();
                System.err.println(e.getMessage());
            }
        }
        return (variant.getId());
    }
