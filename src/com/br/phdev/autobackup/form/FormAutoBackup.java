/*
 * Copyright (C) 2018 Paulo Henrique Gonçalves Bacelar
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.br.phdev.autobackup.form;

import com.br.phdev.autobackup.AutoBackup;
import com.br.phdev.autobackup.IArchive;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author Paulo Henrique Gonçalves Bacelar
 */
public class FormAutoBackup extends JFrame {
    
    private AutoBackup autoBackup;
    
    private JTextField text_path;
    private JComboBox box_interval;
    private JButton button_start;
    
    public FormAutoBackup() {
        super();
        super.setDefaultCloseOperation(EXIT_ON_CLOSE);
        super.setLocationRelativeTo(null);
        super.setSize(700, 200);                
        
        JPanel listPanel = new JPanel();
        listPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        
        JLabel label_path = new JLabel("Insira o local a ser salvo: ");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0;
        c.gridx = 0;
        c.gridy = 0;
        listPanel.add(label_path, c);
                        
        this.text_path = new JTextField();
        this.text_path.setSize(500, 0);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        c.gridx = 1;
        c.gridy = 0;
        listPanel.add(this.text_path, c);    
        
        JLabel label_interval = new JLabel("Insira o intervalo entre backups: ");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0;
        c.gridx = 0;
        c.gridy = 1;
        listPanel.add(label_interval, c);
        
        this.box_interval = new JComboBox(new String[]{"1 min", "2 min", "5 min"});
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        c.gridx = 1;
        c.gridy = 1;
        listPanel.add(this.box_interval, c);
        
        this.button_start = new JButton("Começar");
        this.button_start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String path = text_path.getText().trim();
                    autoBackup = new AutoBackup(path);
                    List<IArchive> list = autoBackup.openFile(path);
                    autoBackup.writeFiles(path + "\\BACKUP", list);                    
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage());
                }
            }
        });
        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipady = 40;
        c.weightx = 1;
        c.gridwidth = 2;
        c.gridx = 0;
        c.gridy = 2;
        listPanel.add(this.button_start, c);
        
        add(listPanel);                
        
        setVisible(true);
    }
    
    public static void main(String[] args) {
        new FormAutoBackup();
    }
    
    private class Timer extends Thread {
        
        @Override
        public void run() {
            
        }
        
    }
    
}
