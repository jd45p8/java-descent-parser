/*
 * Copyright 2019 José Polo <Github https://github.com/jd45p8>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.descentparser.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.util.ArrayList;
import java.io.IOException;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author José Polo <Github https://github.com/jd45p8>
 */
public class FileTools {
    /**
     * Reads a file for storing its lines in a list.
     * @param file File to read.
     * @return List with read lines.
     * @throws IOException if something goes wrong when trying to read the provided file.
     */
    public static ArrayList<String> readFile(File file) throws IOException {
        ArrayList<String> result;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            result = new ArrayList();
            while((line = br.readLine()) != null){
                result.add(line);
            }
        }
        
        return result;
    }
    
    public static File requestFile(){
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.setMultiSelectionEnabled(false);
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Plane text files", "*.txt"));
        fileChooser.showDialog(null, "Seleccionar archivo");
        return fileChooser.getSelectedFile();
    }
}
