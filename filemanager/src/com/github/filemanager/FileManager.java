
package com.github.filemanager;

import com.sun.source.tree.Tree;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import javax.swing.table.*;
import javax.swing.filechooser.FileSystemView;

import javax.imageio.ImageIO;

import java.lang.reflect.Array;
import java.util.*;

import java.io.*;
import java.nio.channels.FileChannel;

import java.net.URL;
import java.util.List;

/**
 *
 * created: 10.11.2019
 *
 * @author David Zashkolny
 * 3 course, comp math
 * Taras Shevchenko National University of Kyiv
 * email: davendiy@gmail.com
 *
 * A basic File Manager.  Requires 1.6+ for the Desktop &amp; SwingWorker
 * classes, amongst other minor things.
 * Includes support classes FileTableModel &amp; FileTreeCellRenderer.

 * TODO Bugs
 * <ul>
 *     <li>Still throws occasional AIOOBEs and NPEs, so some update on
 *     the EDT must have been missed.</li>
 *     <li>Fix keyboard focus issues - especially when functions like
 *     rename/delete etc. are called that update nodes &amp; file lists.</li>
 *     <li>Needs more testing in general.</li>
 *  </ul>

 * TODO Functionality
 * <ul>
 *      <li>Implement Read/Write/Execute checkboxes
 *      <li>Implement Copy
 *      <li>Extra prompt for directory delete (camickr suggestion)
 *      <li>Add File/Directory fields to FileTableModel
 *      <li>Double clicking a directory in the table, should update the tree
 *      <li>Move progress bar?
 *      <li>Add other file display modes (besides table) in CardLayout?
 *      <li>Menus + other cruft?
 *      <li>Implement history/back
 *      <li>Allow multiple selection
 *      <li>Add file search
 * </ul>
 * @version 2011-06-01
 */

public class FileManager {

    /** Title of the application */
    private static final String APP_TITLE = "FileMan";
    /** Used to open/edit/print files. */
    private Desktop desktop;
    /** Provides nice icons and names for files. */
    private FileSystemView fileSystemView;

    /** currently selected File. */
    private File currentFile;

    /** Main GUI container */
    private JPanel gui;

    /** File-system tree. Built Lazily */
    private JTree tree;
    private DefaultTreeModel treeModel;

    /** Directory listing */
    private JTable table;
    private JProgressBar progressBar;
    private JButton stop;


    /** Table model for File[]. */
    private FileTableModel fileTableModel;
    private ListSelectionListener listSelectionListener;
    private boolean cellSizesSet = false;
    private int rowIconPadding = 6;

    /* File details. */
    private JLabel fileName;
//    private JTextField path;
    private JTextField find;
    private JLabel date;
    private JLabel size;
    private JCheckBox readable;
    private JCheckBox writable;
    private JCheckBox executable;
    private JRadioButton isDirectory;
    private JRadioButton isFile;

    private boolean stopFinding = true;

    /* GUI options/containers for new File/Directory creation.  Created lazily. */
    private JPanel newFilePanel;
    private JRadioButton newTypeFile;
    private JTextField name;

    public Container getGui() {
        if (gui==null) {
            gui = new JPanel(new BorderLayout(3,3));
            gui.setBorder(new EmptyBorder(5,5,5,5));

            fileSystemView = FileSystemView.getFileSystemView();
            desktop = Desktop.getDesktop();

            JPanel detailView = new JPanel(new BorderLayout(3,3));
            //fileTableModel = new FileTableModel();


            // ================================ table ==================================================================

            table = new JTable();
            table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            table.setAutoCreateRowSorter(true);
            table.setShowVerticalLines(false);

            listSelectionListener = lse -> {
                int row = table.getSelectionModel().getLeadSelectionIndex();
                setFileDetails( ((FileTableModel)table.getModel()).getFile(row) );
            };
            table.getSelectionModel().addListSelectionListener(listSelectionListener);
            table.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() == 2){
                        int row = table.getSelectionModel().getLeadSelectionIndex();
                        File selected = ((FileTableModel)table.getModel()).getFile(row);
                        if (selected.isDirectory()){
                            TreePath currentPath = findTreePath(selected);
                            if (currentPath != null){
                                DefaultMutableTreeNode node =
                                        (DefaultMutableTreeNode)currentPath.getLastPathComponent();
                                showChildren(node);
                            }

                        } else {
                            try {
                                desktop.open(selected);
                            } catch (Exception ex){
                                showThrowable(ex);
                            }
                        }
                    };
                }
            });
            JScrollPane tableScroll = new JScrollPane(table);
            Dimension d = tableScroll.getPreferredSize();
            tableScroll.setPreferredSize(new Dimension((int)d.getWidth(), (int)d.getHeight()/2));
            detailView.add(tableScroll, BorderLayout.CENTER);

            // ======================================== files tree =====================================================
            DefaultMutableTreeNode root = new DefaultMutableTreeNode();
            treeModel = new DefaultTreeModel(root);

            TreeSelectionListener treeSelectionListener = tse -> {
                DefaultMutableTreeNode node =
                        (DefaultMutableTreeNode)tse.getPath().getLastPathComponent();
                showChildren(node);
                setFileDetails((File)node.getUserObject());
            };

            // show the file system roots.
            File[] roots = fileSystemView.getRoots();
            for (File fileSystemRoot : roots) {
                DefaultMutableTreeNode node = new DefaultMutableTreeNode(fileSystemRoot);
                root.add( node );
                //showChildren(node);
                //
                File[] files = fileSystemView.getFiles(fileSystemRoot, true);
                for (File file : files) {
                    if (file.isDirectory()) {
                        node.add(new DefaultMutableTreeNode(file));
                    }
                }
                //
            }

            tree = new JTree(treeModel);
            tree.setRootVisible(false);
            tree.addTreeSelectionListener(treeSelectionListener);
            tree.setCellRenderer(new FileTreeCellRenderer());
            tree.expandRow(0);
            JScrollPane treeScroll = new JScrollPane(tree);

            // as per trashgod tip
            tree.setVisibleRowCount(15);

            Dimension preferredSize = treeScroll.getPreferredSize();
            Dimension widePreferred = new Dimension(
                    200,
                    (int)preferredSize.getHeight());
            treeScroll.setPreferredSize( widePreferred );


            // ================================= details for a File ====================================================
            JPanel fileMainDetails = new JPanel(new BorderLayout(4,2));
            fileMainDetails.setBorder(new EmptyBorder(0,6,0,6));

            JPanel fileDetailsLabels = new JPanel(new GridLayout(0,1,2,2));
            fileMainDetails.add(fileDetailsLabels, BorderLayout.WEST);

            JPanel fileDetailsValues = new JPanel(new GridLayout(0,1,2,2));
            fileMainDetails.add(fileDetailsValues, BorderLayout.CENTER);


            // ---------------------------------- labels ---------------------------------------------------------------
            fileDetailsLabels.add(new JLabel("File", JLabel.TRAILING));
            fileName = new JLabel();
            fileDetailsValues.add(fileName);

            fileDetailsLabels.add(new JLabel("Find:", JLabel.TRAILING));
            find = new JTextField(5);
            find.setEditable(true);
            fileDetailsValues.add(find);

            fileDetailsLabels.add(new JLabel("Last Modified", JLabel.TRAILING));
            date = new JLabel();
            fileDetailsValues.add(date);
            fileDetailsLabels.add(new JLabel("File size", JLabel.TRAILING));
            size = new JLabel();
            fileDetailsValues.add(size);
            fileDetailsLabels.add(new JLabel("Type", JLabel.TRAILING));

            JPanel flags = new JPanel(new FlowLayout(FlowLayout.LEADING,4,0));
            isDirectory = new JRadioButton("Directory");
            isDirectory.setEnabled(false);
            flags.add(isDirectory);

            isFile = new JRadioButton("File");
            isFile.setEnabled(false);
            flags.add(isFile);
            fileDetailsValues.add(flags);

            int count = fileDetailsLabels.getComponentCount();
            for (int ii=0; ii<count; ii++) {
                fileDetailsLabels.getComponent(ii).setEnabled(false);
            }

            JToolBar toolBar = new JToolBar();
            // mnemonics stop working in a floated toolbar
            toolBar.setFloatable(false);

            // -------------------------------------- buttons ----------------------------------------------------------
            JButton openFile = new JButton("Open");
            openFile.setMnemonic('o');

            openFile.addActionListener(ae -> {
                try {
                    desktop.open(currentFile);
                } catch(Throwable t) {
                    showThrowable(t);
                }
                gui.repaint();
            });
            toolBar.add(openFile);

            JButton findFile = new JButton("Find");
            findFile.setMnemonic(KeyEvent.VK_ENTER);
            findFile.addActionListener(ae -> {
                TreePath currentPath = findTreePath(currentFile);
                if (currentPath != null){
                    DefaultMutableTreeNode currentNode =
                                (DefaultMutableTreeNode)currentPath.getLastPathComponent();
                    stopFinding = false;
                    findChildren(currentNode);
                }
            });
            toolBar.add(findFile);


            JButton editFile = new JButton("Edit");
            editFile.setMnemonic('e');
            editFile.addActionListener(ae -> {
                try {
                    desktop.edit(currentFile);
                } catch(Throwable t) {
                    showThrowable(t);
                }
            });
            toolBar.add(editFile);

            JButton printFile = new JButton("Print");
            printFile.setMnemonic('p');
            printFile.addActionListener(ae -> {
                try {
                    desktop.print(currentFile);
                } catch(Throwable t) {
                    showThrowable(t);
                }
            });
            toolBar.add(printFile);

            // Check the actions are supported on this platform!
            openFile.setEnabled(desktop.isSupported(Desktop.Action.OPEN));
            editFile.setEnabled(desktop.isSupported(Desktop.Action.EDIT));
            printFile.setEnabled(desktop.isSupported(Desktop.Action.PRINT));

            toolBar.addSeparator();

            JButton newFile = new JButton("New");
            newFile.setMnemonic('n');
            newFile.addActionListener(ae -> newFile());
            toolBar.add(newFile);

            JButton copyFile = new JButton("Copy");
            copyFile.setMnemonic('c');
            copyFile.addActionListener(ae -> showErrorMessage("'Copy' not implemented.",
                    "Not implemented."));
            toolBar.add(copyFile);

            JButton renameFile = new JButton("Rename");
            renameFile.setMnemonic('r');
            renameFile.addActionListener(ae -> renameFile());
            toolBar.add(renameFile);

            JButton deleteFile = new JButton("Delete");
            deleteFile.setMnemonic('d');
            deleteFile.addActionListener(ae -> deleteFile());
            toolBar.add(deleteFile);

            toolBar.addSeparator();

            readable = new JCheckBox("Read  ");
            readable.setMnemonic('a');
            //readable.setEnabled(false);
            toolBar.add(readable);

            writable = new JCheckBox("Write  ");
            writable.setMnemonic('w');
            //writable.setEnabled(false);
            toolBar.add(writable);

            executable = new JCheckBox("Execute");
            executable.setMnemonic('x');
            //executable.setEnabled(false);
            toolBar.add(executable);

            JPanel fileView = new JPanel(new BorderLayout(3,3));

            fileView.add(toolBar,BorderLayout.NORTH);
            fileView.add(fileMainDetails,BorderLayout.CENTER);

            detailView.add(fileView, BorderLayout.SOUTH);

            JSplitPane splitPane = new JSplitPane(
                    JSplitPane.HORIZONTAL_SPLIT,
                    treeScroll,
                    detailView);
            gui.add(splitPane, BorderLayout.CENTER);


            // ======================================= progress bar ====================================================
            JPanel simpleOutput = new JPanel(new BorderLayout(3,3));
            progressBar = new JProgressBar();
            simpleOutput.add(progressBar, BorderLayout.EAST);

            stop = new JButton("Stop");
            stop.addActionListener(actionEvent -> stopFinding = true);
            simpleOutput.add(stop, BorderLayout.CENTER);
            stop.setVisible(false);
            progressBar.setVisible(false);

            gui.add(simpleOutput, BorderLayout.SOUTH);

        }
        return gui;
    }

    public void showRootFile() {
        // ensure the main files are displayed
        tree.setSelectionInterval(0,0);
    }

    private TreePath findTreePath(File find) {
        for (int ii=0; ii<tree.getRowCount(); ii++) {
            TreePath treePath = tree.getPathForRow(ii);
            Object object = treePath.getLastPathComponent();
            DefaultMutableTreeNode node = (DefaultMutableTreeNode)object;
            File nodeFile = (File)node.getUserObject();

            if (nodeFile.equals(find)) {
                return treePath;
            }
        }
        // not found!
        return null;
    }

    private void renameFile() {
        if (currentFile==null) {
            showErrorMessage("No file selected to rename.","Select File");
            return;
        }

        String renameTo = JOptionPane.showInputDialog(gui, "New Name");
        if (renameTo!=null) {
            try {
                boolean directory = currentFile.isDirectory();
                TreePath parentPath = findTreePath(currentFile.getParentFile());
                DefaultMutableTreeNode parentNode =
                        (DefaultMutableTreeNode)parentPath.getLastPathComponent();

                boolean renamed = currentFile.renameTo(new File(
                        currentFile.getParentFile(), renameTo));
                if (renamed) {
                    if (directory) {
                        // rename the node..

                        // delete the current node..
                        TreePath currentPath = findTreePath(currentFile);
                        System.out.println(currentPath);
                        DefaultMutableTreeNode currentNode =
                                (DefaultMutableTreeNode)currentPath.getLastPathComponent();

                        treeModel.removeNodeFromParent(currentNode);

                        // add a new node..
                    }

                    showChildren(parentNode);
                } else {
                    String msg = "The file '" +
                            currentFile +
                            "' could not be renamed.";
                    showErrorMessage(msg,"Rename Failed");
                }
            } catch(Exception t) {
                showThrowable(t);
            }
        }
        gui.repaint();
    }

    private void deleteFile() {
        if (currentFile==null) {
            showErrorMessage("No file selected for deletion.","Select File");
            return;
        }

        int result = JOptionPane.showConfirmDialog(
                gui,
                "Are you sure you want to delete this file?",
                "Delete File",
                JOptionPane.YES_NO_OPTION
        );
        if (result==JOptionPane.YES_OPTION) {
            try {
                System.out.println("currentFile: " + currentFile);
                TreePath parentPath = findTreePath(currentFile.getParentFile());
                System.out.println("parentPath: " + parentPath);
                if (parentPath != null) {
                        DefaultMutableTreeNode parentNode =
                                (DefaultMutableTreeNode) parentPath.getLastPathComponent();
                        System.out.println("parentNode: " + parentNode);

                    boolean directory = currentFile.isDirectory();
                    boolean deleted = currentFile.delete();
                    if (deleted) {
                        if (directory) {
                            // delete the node..
                            TreePath currentPath = findTreePath(currentFile);
                            System.out.println(currentPath);
                            DefaultMutableTreeNode currentNode =
                                    (DefaultMutableTreeNode) currentPath.getLastPathComponent();

                            treeModel.removeNodeFromParent(currentNode);
                        }

                        showChildren(parentNode);

                    } else {
                        String msg = "The file '" +
                                currentFile +
                                "' could not be deleted.";
                        showErrorMessage(msg,"Delete Failed");
                    }
                }
            } catch(Throwable t) {
                showThrowable(t);
            }
        }
        gui.repaint();
    }

    private void newFile() {
        if (currentFile==null) {
            showErrorMessage("No location selected for new file.","Select Location");
            return;
        }

        if (newFilePanel==null) {
            newFilePanel = new JPanel(new BorderLayout(3,3));

            JPanel southRadio = new JPanel(new GridLayout(1,0,2,2));
            newTypeFile = new JRadioButton("File", true);
            JRadioButton newTypeDirectory = new JRadioButton("Directory");
            ButtonGroup bg = new ButtonGroup();
            bg.add(newTypeFile);
            bg.add(newTypeDirectory);
            southRadio.add( newTypeFile );
            southRadio.add( newTypeDirectory );

            name = new JTextField(15);

            newFilePanel.add( new JLabel("Name"), BorderLayout.WEST );
            newFilePanel.add( name );
            newFilePanel.add( southRadio, BorderLayout.SOUTH );
        }

        int result = JOptionPane.showConfirmDialog(
                gui,
                newFilePanel,
                "Create File",
                JOptionPane.OK_CANCEL_OPTION);
        if (result==JOptionPane.OK_OPTION) {
            try {
                boolean created;
                File parentFile = currentFile;
                if (!parentFile.isDirectory()) {
                    parentFile = parentFile.getParentFile();
                }
                File file = new File( parentFile, name.getText() );
                if (newTypeFile.isSelected()) {
                    created = file.createNewFile();
                } else {
                    created = file.mkdir();
                }
                if (created) {

                    TreePath parentPath = findTreePath(parentFile);
                    DefaultMutableTreeNode parentNode =
                            (DefaultMutableTreeNode)parentPath.getLastPathComponent();

                    if (file.isDirectory()) {
                        // add the new node..
                        DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(file);

                        TreePath currentPath = findTreePath(currentFile);
                        DefaultMutableTreeNode currentNode =
                                (DefaultMutableTreeNode)currentPath.getLastPathComponent();

                        treeModel.insertNodeInto(newNode, parentNode, parentNode.getChildCount());
                    }

                    showChildren(parentNode);
                } else {
                    String msg = "The file '" +
                            file +
                            "' could not be created.";
                    showErrorMessage(msg, "Create Failed");
                }
            } catch(Throwable t) {
                showThrowable(t);
            }
        }
        gui.repaint();
    }

    private void showErrorMessage(String errorMessage, String errorTitle) {
        JOptionPane.showMessageDialog(
                gui,
                errorMessage,
                errorTitle,
                JOptionPane.ERROR_MESSAGE
        );
    }

    private void showThrowable(Throwable t) {
        t.printStackTrace();
        JOptionPane.showMessageDialog(
                gui,
                t.toString(),
                t.getMessage(),
                JOptionPane.ERROR_MESSAGE
        );
        gui.repaint();
    }

    /** Update the table on the EDT */
    private void setTableData(final File[] files, boolean add) {
        SwingUtilities.invokeLater(() -> {
            if (fileTableModel==null) {
                fileTableModel = new FileTableModel();
                table.setModel(fileTableModel);
            }
            table.getSelectionModel().removeListSelectionListener(listSelectionListener);
            if (add) {
                fileTableModel.addFiles(files);
            } else {
                fileTableModel.setFiles(files);
            }
            table.getSelectionModel().addListSelectionListener(listSelectionListener);
            if (!cellSizesSet) {
                Icon icon = fileSystemView.getSystemIcon(files[0]);

                // size adjustment to better account for icons
                table.setRowHeight( icon.getIconHeight()+rowIconPadding );

                setColumnWidth(0,-1);
                setColumnWidth(3,60);
                table.getColumnModel().getColumn(3).setMaxWidth(120);
                setColumnWidth(4,-1);
                setColumnWidth(5,-1);
                setColumnWidth(6,-1);
                setColumnWidth(7,-1);
                setColumnWidth(8,-1);
                setColumnWidth(9,-1);

                cellSizesSet = true;
            }
        });
    }

    private void setColumnWidth(int column, int width) {
        TableColumn tableColumn = table.getColumnModel().getColumn(column);
        if (width<0) {
            // use the preferred width of the header..
            JLabel label = new JLabel( (String)tableColumn.getHeaderValue() );
            Dimension preferred = label.getPreferredSize();
            // altered 10->14 as per camickr comment.
            width = (int)preferred.getWidth()+14;
        }
        tableColumn.setPreferredWidth(width);
        tableColumn.setMaxWidth(width);
        tableColumn.setMinWidth(width);
    }

    private void findChildren(final DefaultMutableTreeNode node){
        String pattern = find.getText();
        if (pattern.isEmpty()){
            return;
        }
        progressBar.setVisible(true);
        progressBar.setIndeterminate(true);
        stop.setVisible(true);
        File[] empty = new File[0];
        fileTableModel.setFiles(empty);
        SwingWorker<Void, File> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() throws Exception {
                File file = (File) node.getUserObject();
                if (file.isDirectory()){
                    File[] files = fileSystemView.getFiles(file, true);
                    ArrayDeque<File> queue = new ArrayDeque<>(Arrays.asList(files));
                    while (!queue.isEmpty()){
                        if (stopFinding){
                            break;
                        }

                        File tmp = queue.pop();
                        if (tmp.isDirectory()){
                            queue.addAll(Arrays.asList(fileSystemView.getFiles(tmp, true)));
                        }
                        String name = tmp.getName();
                        if (name.contains(pattern)){
                            publish(tmp);

                        }
                    }

                }
                return null;
            }

            @Override
            protected void process(List<File> chunks){
                File[] tmp = new File[chunks.size()];
                for (int i=0; i < chunks.size(); i++){
                    tmp[i] = chunks.get(i);
                }
                setTableData(tmp, true);
            }

            protected void done(){
                progressBar.setIndeterminate(false);
                progressBar.setVisible(false);
                stop.setVisible(false);
            }
        };
        worker.execute();
    }

    /** Add the files that are contained within the directory of this node.
     Thanks to Hovercraft Full Of Eels. */
    private void showChildren(final DefaultMutableTreeNode node) {
        tree.setEnabled(false);
        progressBar.setVisible(true);
        progressBar.setIndeterminate(true);

        SwingWorker<Void, File> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() throws Exception{
                File file = (File) node.getUserObject();
                if (file.isDirectory()) {
                    File[] files = fileSystemView.getFiles(file, true);   //!!
                    if (node.isLeaf()) {
                        for (File child : files) {
                            if (child.isDirectory()) {
                                publish(child);
                            }
                        }
                    }
                    setTableData(files, false);
                }
                return null;
            }

            @Override
            protected void process(List<File> chunks) {
                for (File child : chunks) {
                    node.add(new DefaultMutableTreeNode(child));
                }
            }

            @Override
            protected void done() {
                progressBar.setIndeterminate(false);
                progressBar.setVisible(false);
                tree.setEnabled(true);
            }
        };
        worker.execute();
    }

    /** Update the File details view with the details of this File. */
    private void setFileDetails(File file) {
        currentFile = file;
        Icon icon = fileSystemView.getSystemIcon(file);
        fileName.setIcon(icon);
        fileName.setText(fileSystemView.getSystemDisplayName(file));
//        path.setText(file.getPath());
        date.setText(new Date(file.lastModified()).toString());
        size.setText(file.length() + " bytes");
        readable.setSelected(file.canRead());
        writable.setSelected(file.canWrite());
        executable.setSelected(file.canExecute());
        isDirectory.setSelected(file.isDirectory());

        isFile.setSelected(file.isFile());

        JFrame f = (JFrame)gui.getTopLevelAncestor();
        if (f!=null) {
            f.setTitle(
                    APP_TITLE +
                            " :: " +
                            fileSystemView.getSystemDisplayName(file) );
        }

        gui.repaint();
    }

    public static boolean copyFile(File from, File to) throws IOException {

        boolean created = to.createNewFile();

        if (created) {
            try (FileChannel fromChannel = new FileInputStream(from).getChannel(); FileChannel toChannel = new FileOutputStream(to).getChannel()) {

                toChannel.transferFrom(fromChannel, 0, fromChannel.size());

                // set the flags of the to the same as the from
                to.setReadable(from.canRead());
                to.setWritable(from.canWrite());
                to.setExecutable(from.canExecute());
            }
        }
        return created;

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    // Significantly improves the look of the output in
                    // terms of the file names returned by FileSystemView!
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch(Exception weTried) {
                    System.out.println(weTried.toString());
                }
                JFrame f = new JFrame(APP_TITLE);
                f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                FileManager fileManager = new FileManager();
                f.setContentPane(fileManager.getGui());

                try {
                    URL urlBig = fileManager.getClass().getResource("fm-icon-32x32.png");
                    URL urlSmall = fileManager.getClass().getResource("fm-icon-16x16.png");
                    ArrayList<Image> images = new ArrayList<>();
                    images.add( ImageIO.read(urlBig) );
                    images.add( ImageIO.read(urlSmall) );
                    f.setIconImages(images);
                } catch(Exception weTried) {
                    System.out.println(weTried.toString());
                }

                f.pack();
                f.setLocationByPlatform(true);
                f.setMinimumSize(f.getSize());
                f.setVisible(true);

                fileManager.showRootFile();
            }
        });
    }
}

