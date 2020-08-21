package com.sudhindra.delta_onsites_task_4.models;

import android.util.Log;

import com.sudhindra.delta_onsites_task_4.R;

import java.io.File;
import java.util.ArrayList;

public class FileItem {

    public static final int DOWN_ARROW = R.drawable.down_arrow, RIGHT_ARROW = R.drawable.right_arrow;
    public static final int FOLDER_ICON = R.drawable.folder_icon, FILE_ICON = R.drawable.file_icon;

    private static final String TAG = "FileItem";
    private File file;
    private boolean expanded = false;

    private ArrayList<FileItem> subFileItems = null;
    private boolean showEmptyFolder = false;

    public FileItem(File file) {
        this.file = file;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    public ArrayList<FileItem> getSubFileItems() {
        return subFileItems;
    }

    public boolean isShowEmptyFolder() {
        return showEmptyFolder;
    }

    public void setShowEmptyFolder(boolean showEmptyFolder) {
        this.showEmptyFolder = showEmptyFolder;
    }
}
