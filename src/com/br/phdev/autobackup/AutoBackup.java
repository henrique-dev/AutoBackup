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
package com.br.phdev.autobackup;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Paulo Henrique Gonçalves Bacelar
 */
public class AutoBackup{               
    
    private final String currentPath;
    
    public AutoBackup(String pathToBackup) {               
        //this.currentPath = System.getProperty("user.dir");
        this.currentPath = pathToBackup;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        //AutoBackup autoBackup = new AutoBackup();
        //String path = System.getProperty("user.dir");
        //List<Readable> lista = autoBackup.openFile(path);
        //autoBackup.writeFiles(path + "\\BACKUP", lista);
                        
    }        
    
    public List<IArchive> openFile(String path) {
        List<IArchive> tempList = new ArrayList<>();
        File source = new File(path);
        File[] files = source.listFiles();
        
        for (File f : files) {                
            Archive archive = new Archive();                
            if (f.isFile()) {
                archive.setPath(f.getPath());
                File file = new File(archive.getPath());                    
                byte[] bytes = getBytesFromFile(file);
                archive.setName(f.getName());                
                archive.setBytes(bytes);
                tempList.add(archive);
            } else if (f.isDirectory()) {                
                if (f.getPath().equals((this.currentPath + "\\BACKUP"))) {
                    System.out.println(f.getPath()+ " --------> " + this.currentPath + "\\BACKUP");
                    continue;                    
                }
                Directory directory = new Directory();
                directory.setName(f.getName());
                directory.setPath(f.getPath());
                directory.setArchives(openFile(f.getPath()));  
                tempList.add(directory);
            }
        }      
        return tempList;
    }
    
    public void writeFiles(String pathDestiny, List<IArchive> arquivos) {
        File destiny = new File(pathDestiny);
        if (!destiny.exists())
            destiny.mkdir();
        
        FileOutputStream fos = null;
        try {    
            for (IArchive readable : arquivos) {
                if (readable instanceof Directory) {
                    Directory directory = (Directory)readable;
                    String subPath = destiny + "\\" + directory.getName();                    
                    writeFiles(subPath, directory.getArchives());
                } else if (readable instanceof Archive) {    
                    Archive archive = (Archive)readable;
                    fos = new FileOutputStream(pathDestiny + "\\" + archive.getName());
                    fos.write(archive.getBytes());
                    fos.flush();
                }                
            }
            if (fos != null)
                fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //System.out.println(arquivos.size());
    }
    
    private byte[] getBytesFromFile(File file) {
        byte[] bytes = new byte[(int)file.length()];
        try (FileInputStream fis = new FileInputStream(file)) {
            fis.read(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bytes;
    }
    
}
