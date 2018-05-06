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
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
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
    private JButton button_stop;
    
    private Timer timerToBackup;

    public FormAutoBackup() {
        super();
        super.setTitle("AutoBackup");
        super.setDefaultCloseOperation(EXIT_ON_CLOSE);        
        super.setSize(500, 200);

        JPanel listPanel = new JPanel();
        listPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 5, 5, 5);

        JLabel label_path = new JLabel("Local: ");
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

        JLabel label_interval = new JLabel("Intervalo: ");
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
                File file = new File(text_path.getText());
                if (!file.exists()) {
                    JOptionPane.showMessageDialog(listPanel, "O caminho inserido não existe!");
                } else {                    
                    switch (box_interval.getSelectedIndex()) {
                        case 0: 
                            timerToBackup = new Timer(60);                                                    
                            break;
                        case 1:
                            timerToBackup = new Timer(120);
                            break;
                        case 2:
                            timerToBackup = new Timer(300);
                            break;
                    }
                    timerToBackup.setRunning(true);
                    timerToBackup.start();
                    button_start.setEnabled(false);
                    text_path.setEditable(false);
                    text_path.setEnabled(false);
                    box_interval.setEnabled(false);
                    button_stop.setEnabled(true);                    
                }                                
            }
        });
        c.fill = GridBagConstraints.HORIZONTAL;
        //c.ipady = 40;
        c.ipadx = 50;        
        c.insets = new Insets(10, 10, 0, 10);
        c.weightx = 1;
        c.gridwidth = 2;
        c.gridx = 0;
        c.gridy = 2;
        listPanel.add(this.button_start, c);
        
        this.button_stop = new JButton("Parar");
        this.button_stop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timerToBackup.setRunning(false);
                try {
                    timerToBackup.join();
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                } finally {
                    timerToBackup = null;
                    button_start.setEnabled(true);
                    text_path.setEditable(true);
                    text_path.setEnabled(true);
                    box_interval.setEnabled(true);
                    button_stop.setEnabled(false);
                }
            }
        });
        this.button_stop.setEnabled(false);
        c.fill = GridBagConstraints.HORIZONTAL;
        //c.ipady = 40;
        c.ipadx = 50;
        c.weightx = 1;
        c.gridwidth = 3;
        c.gridx = 0;
        c.gridy = 3;
        listPanel.add(this.button_stop, c);

        add(listPanel);

        super.setLocationRelativeTo(null);
        setVisible(true);        
    }

    private void performBackup() {
        try {
            String path = text_path.getText().trim();
            autoBackup = new AutoBackup(path);
            List<IArchive> list = autoBackup.openFile(path);
            autoBackup.writeFiles(path + "\\BACKUP", list);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
    }

    public static void main(String[] args) {
        new FormAutoBackup();
    }

    private class Timer extends Thread {

        private long startTime;        
        private final long backupTime;
        
        private boolean running;
        
        public Timer(long backupTime) {
            this.backupTime = backupTime;
        }

        @Override
        public void run() {
            startTime = System.nanoTime();

            while (this.running) {
                long currentTime = (System.nanoTime() - startTime) / 1000000000;
                if (currentTime > backupTime) {
                    performBackup();
                    startTime = System.nanoTime();
                }
                //System.out.println(currentTime);
            }
        }

        public boolean isRunning() {
            return running;
        }

        public void setRunning(boolean running) {
            this.running = running;
        }                  

    }

}
