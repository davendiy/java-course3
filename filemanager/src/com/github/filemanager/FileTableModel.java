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

 * Class that implements table data model: storing getting necessary elements
 * of table (cells).
 *
 * All the files are stored in one array. i-th row <-> i-th file.
 *
 * Supports the adding and setting new files.
 */
class FileTableModel extends AbstractTableModel {

    private File[] files;
    private FileSystemView fileSystemView = FileSystemView.getFileSystemView();

    // all the columns
    private String[] columns = {
            "Icon",
            "File",
            "Size",
            "Type",
            "Last Modified",
            "R",               // readable
            "W",               // writable
            "E",               // editable
    };

    /**
     * Create empty data model.
     */
    FileTableModel() {
        this(new File[0]);
    }

    /**
     * Create data model with given files.
     */
    FileTableModel(File[] files) {
        this.files = files;
    }

    /**
     * Get information about required cell.
     * @param row      - index of row (same as index in this.files)
     * @param column   - index of column (same as index in this.columns)
     * @return - corresponding object for each type of information
     */
    @Override
    public Object getValueAt(int row, int column) {
        File file = files[row];
        switch (column) {
            case 0:          // icon of a file
                return fileSystemView.getSystemIcon(file);
            case 1:          // name
                return fileSystemView.getSystemDisplayName(file);
            case 2:          // size only for files
                return (file.isDirectory()) ? "" : humanReadableByteCount(file.length(), true);
            case 3:          // type of file
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
            case 4:        // date of last modification
                return file.lastModified();
            case 5:
                return file.canRead();
            case 6:
                return file.canWrite();
            case 7:
                return file.canExecute();
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
                return Boolean.class;
        }
        return String.class;
    }

    /**
     * Auxiliary method for converting bytes to human-readable view.
     * @param bytes  - number of bytes
     * @param si     - true if you need to get the simplified variant (kB, MB, where k ~ 1000, M ~ 1000 000)
     *                 false if you need to get the digital view (MiB = 1024 KiB and so on)
     * @return - corresponding string
     */
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
