package com.acceler8tion.Saturn.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class FileStream {

    private FileStream(){

    }

    public static String read(String path) throws IOException{
        FileReader fr = new FileReader(path);
        BufferedReader buf = new BufferedReader(fr);
        StringBuilder result = new StringBuilder(buf.readLine());
        String t;
        while((t=buf.readLine()) != null){
            result.append("\\n").append(t);
        }
        return result.toString();
    }

    public static String read(String path, String replace) {
        FileReader fr;
        try {
            fr = new FileReader(path);
            BufferedReader buf = new BufferedReader(fr);
            StringBuilder result = new StringBuilder(buf.readLine());
            String t;
            while((t=buf.readLine()) != null){
                result.append("\\n").append(t);
            }
            return result.toString();
        } catch (Exception e) {
            return replace;
        }
    }

    public static void write(String path, String text) throws IOException{
        FileWriter fw = new FileWriter(path);
        fw.write(text);
        fw.flush();
    }

    public static void write(String path, Object what) throws IOException{
        FileWriter fw = new FileWriter(path);
        fw.write(what.toString());
        fw.flush();
    }

    public static void writeAsProperties(String path, Properties prop) throws IOException {
        List<String> props = new ArrayList<>();
        for(String key : prop.stringPropertyNames()){
            props.add(key+"="+prop.getProperty(key));
        }
        write(path, String.join("\n", props.toArray(new String[]{})));
    }

    public static void remove(String path){
        File f = new File(path);
        if(f.exists()) f.delete();
    }

    public static void append(String path, String text) throws IOException{
        String file = read(path);
        file += text;
        write(path, file);
    }
}