package com.github.filemanager;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.AbstractTableModel;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;

/**
 * created: 10.11.2019
 *
 * @author David Zashkolny
 * 3 course, comp math
 * Taras Shevchenko National University of Kyiv
 * email: davendiy@gmail.com
 *
 * A TableModel to hold File[].
 */


class FileTableModel extends AbstractTableModel {

    private File[] files;
    private FileSystemView fileSystemView = FileSystemView.getFileSystemView();
    private String[] columns = {
            "Icon",
            "File",
            "Size",
            "Type",
            "Last Modified",
            "R",
            "W",
            "E",
//            "D",
//            "F",
    };

    FileTableModel() {
        this(new File[0]);
    }

    FileTableModel(File[] files) {
        this.files = files;
    }

    @Override
    public Object getValueAt(int row, int column) {
        File file = files[row];
        switch (column) {
            case 0:
                return fileSystemView.getSystemIcon(file);
            case 1:
                return fileSystemView.getSystemDisplayName(file);
            case 3:
                Path path = file.toPath();
                String mimeType = "";
                try {
                    mimeType = Files.probeContentType(path);
                    if (mimeType == null){
                        if (file.isDirectory()){
                            mimeType = "";
                        } else {
                            mimeType = "File";
                        }
                    }
                } catch (Exception e){
                    System.out.println("Error with getting type of file..");
                    e.printStackTrace();
                }

                return mimeType;
            case 2:
                return humanReadableByteCount(file.length(), true);
            case 4:
                return file.lastModified();
            case 5:
                return file.canRead();
            case 6:
                return file.canWrite();
            case 7:
                return file.canExecute();
//            case 8:
//                return file.isDirectory();
//            case 9:
//                return file.isFile();
            default:
                System.err.println("Logic Error");
        }
        return "";
    }

    @Override
    public int getColumnCount() {
        return columns.length;
    }

    @Override
    public Class<?> getColumnClass(int column) {
        switch (column) {
            case 0:
                return ImageIcon.class;
            case 2:
                return Long.class;
            case 4:
                return Date.class;
            case 5:
            case 6:
            case 7:
//            case 8:
//            case 9:
                return Boolean.class;
        }
        return String.class;
    }

    public static String humanReadableByteCount(long bytes, boolean si) {
        int unit = si ? 1000 : 1024;
        if (bytes < unit) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp-1) + (si ? "" : "i");
        return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
    }

    @Override
    public String getColumnName(int column) {
        return columns[column];
    }

    @Override
    public int getRowCount() {
        return files.length;
    }

    public File getFile(int row) {
        return files[row];
    }

    public void setFiles(File[] files) {
        this.files = files;
        fireTableDataChanged();
    }

    public void addFiles(File[] files){
        File[] res = new File[this.files.length + files.length];
        System.arraycopy(this.files, 0, res, 0, this.files.length);
        System.arraycopy(files, 0, res, this.files.length, files.length);
        this.files = res;
        fireTableDataChanged();
    }
}
