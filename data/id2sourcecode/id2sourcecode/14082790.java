    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == urlField) {
        }
        if (event.getSource() == tableSearchDownloadComic) {
            searchDownloadComic(downloadTablePopupRow);
        } else if (event.getSource() == tableSearchBookmarkComic) {
            searchDownloadComic(bookmarkTablePopupRow);
        } else if (event.getSource() == tableSearchRecordComic) {
            searchDownloadComic(recordTablePopupRow);
        }
        if (event.getSource() == tableOpenDownloadFile) {
            openDownloadFile(downloadTablePopupRow);
        } else if (event.getSource() == tableOpenBookmarkFile) {
            openDownloadFile(bookmarkTablePopupRow);
        } else if (event.getSource() == tableOpenRecordFile) {
            openDownloadFile(recordTablePopupRow);
        }
        if (event.getSource() == tableOpenDownloadURL) {
            openDownloadURL(downloadTablePopupRow);
        } else if (event.getSource() == tableOpenBookmarkURL) {
            openDownloadURL(bookmarkTablePopupRow);
        } else if (event.getSource() == tableOpenRecordURL) {
            openDownloadURL(recordTablePopupRow);
        } else if (event.getSource() == tableOpenDownloadDirectoryItem) {
            openDownloadDirectory(downloadTablePopupRow);
        } else if (event.getSource() == tableOpenBookmarkDirectoryItem) {
            openDownloadDirectory(bookmarkTablePopupRow);
        } else if (event.getSource() == tableOpenRecordDirectoryItem) {
            openDownloadDirectory(recordTablePopupRow);
        }
        if (event.getSource() == pasteSystemClipboardItem) {
            String clipString = new SystemClipBoard().getClipString();
            urlField.setText(clipString);
        }
        if (event.getSource() == tableAddBookmarkFromDownloadItem) {
            addBookmark(downloadTablePopupRow);
        } else if (event.getSource() == tableAddBookmarkFromRecordItem) {
            addBookmark(recordTablePopupRow);
        }
        if (event.getSource() == tableAddMissionFromBookmarkItem) {
            addMission(bookmarkTablePopupRow);
        } else if (event.getSource() == tableAddMissionFromRecordItem) {
            addMission(recordTablePopupRow);
        }
        if (event.getSource() == tableRechoiceVolumeItem) {
            if (!Flag.downloadingFlag) {
                rechoiceVolume(downloadTablePopupRow);
            } else {
                JOptionPane.showMessageDialog(this, "目前正下載中，無法重新選擇集數", "提醒訊息", JOptionPane.INFORMATION_MESSAGE);
            }
        }
        if (event.getSource() == tableDeleteMissionItem) {
            if (!Flag.downloadingFlag) {
                deleteMission(downloadTablePopupRow);
            } else {
                JOptionPane.showMessageDialog(this, "目前正下載中，無法刪除任務", "提醒訊息", JOptionPane.INFORMATION_MESSAGE);
            }
        }
        if (event.getSource() == tableDeleteAllUnselectedMissionItem) {
            if (!Flag.downloadingFlag) {
                deleteAllUnselectedMission();
            } else {
                JOptionPane.showMessageDialog(this, "目前正下載中，無法刪除任務", "提醒訊息", JOptionPane.INFORMATION_MESSAGE);
            }
        }
        if (event.getSource() == tableDeleteAllDoneMissionItem) {
            if (!Flag.downloadingFlag) {
                deleteAllDoneMission();
            } else {
                JOptionPane.showMessageDialog(this, "目前正下載中，無法刪除任務", "提醒訊息", JOptionPane.INFORMATION_MESSAGE);
            }
        }
        if (event.getSource() == tableMoveToRoofItem) {
            moveMissionToRoof(downloadTablePopupRow);
        }
        if (event.getSource() == tableMoveToFloorItem) {
            moveMissionToFloor(downloadTablePopupRow);
        }
        if (event.getSource() == tableDeleteBookmarkItem) {
            deleteBookmark(bookmarkTablePopupRow);
        }
        if (event.getSource() == tableDeleteRecordItem) {
            deleteRecord(recordTablePopupRow);
        }
        if (event.getSource() == trayShowItem) {
            setVisible(true);
            setState(Frame.NORMAL);
            SystemTray.getSystemTray().remove(trayIcon);
        }
        if (event.getSource() == button[ButtonEnum.ADD]) {
            logFrame.redirectSystemStreams();
            testDownload();
            String urlString = urlField.getText();
            parseURL(args, false, false, 0);
            args = null;
        }
        if (event.getSource() == button[ButtonEnum.DOWNLOAD] || event.getSource() == trayStartItem) {
            if (Flag.downloadingFlag || Flag.parseUrlFlag) {
                JOptionPane.showMessageDialog(this, "目前正下載中，不提供直接下載，請按「加入」來加入下載任務。", "提醒訊息", JOptionPane.INFORMATION_MESSAGE);
            } else {
                logFrame.redirectSystemStreams();
                Run.isAlive = true;
                stateBar.setText("開始下載中...");
                tabbedPane.setSelectedIndex(TabbedPaneEnum.MISSION);
                Flag.parseUrlFlag = true;
                parseURL(args, true, false, 0);
                args = null;
            }
        }
        if (event.getSource() == button[ButtonEnum.STOP] || event.getSource() == trayStopItem) {
            Run.isAlive = false;
            Flag.allowDownloadFlag = Flag.downloadingFlag = Flag.parseUrlFlag = false;
            stateBar.setText("所有下載任務停止");
            trayIcon.setToolTip("JComicDownloader");
        }
        if (event.getSource() == button[ButtonEnum.OPTION]) {
            new Thread(new Runnable() {

                public void run() {
                    new OptionFrame();
                }
            }).start();
        }
        if (event.getSource() == button[ButtonEnum.INFORMATION]) {
            new Thread(new Runnable() {

                public void run() {
                    final InformationFrame frame = new InformationFrame();
                    new Thread(new Runnable() {

                        public void run() {
                            frame.setNewestVersion();
                        }
                    }).start();
                }
            }).start();
        }
        if (event.getSource() == button[ButtonEnum.CLEAR]) {
            int choice = JOptionPane.showConfirmDialog(this, "請問是否要將目前內容全部清空？", "提醒訊息", JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION) {
                if (tabbedPane.getSelectedIndex() == TabbedPaneEnum.MISSION) {
                    clearMission();
                } else if (tabbedPane.getSelectedIndex() == TabbedPaneEnum.BOOKMARK) {
                    clearBookmark();
                } else if (tabbedPane.getSelectedIndex() == TabbedPaneEnum.RECORD) {
                    clearRecord();
                }
            }
        }
        if (event.getSource() == button[ButtonEnum.EXIT] || event.getSource() == trayExitItem) {
            int choice = JOptionPane.showConfirmDialog(this, "請問是否要關閉JComicDownloader？", "提醒訊息", JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION) {
                exit();
            }
        }
    }
