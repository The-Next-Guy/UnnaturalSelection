package com.xemplarsoft.nnt.builder;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Main extends JFrame implements ActionListener, MouseListener {
    public Drawspace ds;
    public JPanel content;

    public Main(){
        ds = new Drawspace();
        content = new JPanel();

        GridBagLayout gbl = new GridBagLayout();
        gbl.rowHeights = new int[]{48, 256, -1};
        gbl.columnWidths = new int[]{-1, 256, -1};
        gbl.rowWeights = new double[]{0, 1, 0};
        gbl.columnWeights = new double[]{0, 1, 0};
        content.setLayout(gbl);

        JMenuBar menuBar = new JMenuBar();

        JMenu file = new JMenu("File");

        JMenuItem file_new = new JMenuItem("New");
        file_new.setActionCommand("file:new");
        file_new.addActionListener(this);

        JMenuItem file_save = new JMenuItem("Save");
        file_save.setActionCommand("file:save");
        file_save.addActionListener(this);

        JMenuItem file_saveas = new JMenuItem("Save As");
        file_saveas.setActionCommand("file:saveas");
        file_saveas.addActionListener(this);

        file.add(file_new);
        file.add(new JSeparator(JSeparator.HORIZONTAL));
        file.add(file_save);
        file.add(file_saveas);

        JMenu about = new JMenu("About");

        JMenuItem about_author = new JMenuItem("Author");
        about_author.setToolTipText("Rohan Loomis a.k.a The_Next_Guy");

        JMenuItem about_website = new JMenuItem("Website");
        about_website.setToolTipText("Xemplar Softworks | https://xemplarsoft.com");

        about.add(about_author);
        about.add(about_website);

        menuBar.add(file);
        menuBar.add(about);

        setJMenuBar(menuBar);

        Dimension tool_button_size = new Dimension(48, 48);
        JToolBar tools = new JToolBar();
        JButton tool_pencil = new JButton();
        tool_pencil.setActionCommand("tool:pencil");
        tool_pencil.addActionListener(this);
        tool_pencil.setMinimumSize(tool_button_size);
        tool_pencil.setPreferredSize(tool_button_size);
        tool_pencil.setMaximumSize(tool_button_size);
        tools.add(tool_pencil);



        ds.setMinimumSize(new Dimension(256, 256));
        ds.setPreferredSize(new Dimension(256, 256));
        GridBagConstraints gbc_ds = new GridBagConstraints(1, 1, 1, 1, 1, 1,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0);
        content.add(ds, gbc_ds);

        content.setMinimumSize(new Dimension(800, 480));
        content.setPreferredSize(new Dimension(800, 480));
        setContentPane(content);

        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Unnatural Selection Scenario Designer");
        setVisible(true);

        ds.repaint();
    }

    public void actionPerformed(ActionEvent actionEvent) {

    }

    public void mouseClicked(MouseEvent mouseEvent) {
        
    }
    public void mousePressed(MouseEvent mouseEvent) {

    }
    public void mouseReleased(MouseEvent mouseEvent) {

    }
    public void mouseEntered(MouseEvent mouseEvent) {

    }
    public void mouseExited(MouseEvent mouseEvent) {

    }

    public static void main(String[] args){
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e){
            e.printStackTrace();
        }
        new Main();
    }
}
