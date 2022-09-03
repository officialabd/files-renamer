package com.tools.controls;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.util.regex.Pattern;

import com.tools.utilities.Dialogs;

public class FileProcessor {

    public static final String ONLY_FILES = "Only files";
    public static final String ONLY_FOLDERS = "Only folders";
    public static final String ALL = "All";

    public static Message renameFiles(String dirPath, String range, boolean keep, boolean allowNumbers,
            boolean askForInput, String prefix, String afterNumber, String postfix) {
        int counter = 0;
        File[] files = getFiles(dirPath);
        if (files.length <= 0) {
            return new Message(Message.NORMAL_TYPE, "No files found due to this directory is empty.",
                    Message.NORMAL_COLOR);
        }
        String codeFormat = "";
        for (int i = 0; i < ((int) Math.log(files.length)); i++) {
            codeFormat += "0";
        }
        String num = "";
        for (File f : files) {

            if (allowNumbers)
                num = toCodeNumber(codeFormat, counter) + afterNumber;
            Message msg = renameFile(dirPath, f, range, prefix, num, postfix, keep, askForInput);
            if (msg.getType() == -1) {
                return msg;
            }
            if (msg.getType() == 1)
                counter++;
        }
        if (counter == 0)
            return new Message(Message.NORMAL_TYPE, "No file found!", Message.NORMAL_COLOR);
        return new Message(Message.SUCCESS_TYPE, "Done " + counter + " files/folders manipulated! Rename more :)",
                Message.SUCCESS_COLOR);
    }

    public static Message renameFile(String dirPath, File oldName, String range, String prefix, String num,
            String postfix, boolean keep, boolean askForInput) {
        String tempOldName = "";
        if (range.equals(ONLY_FILES) && oldName.isFile() ||
                range.equals(ONLY_FOLDERS) && oldName.isDirectory() ||
                range.equals(ALL)) {
            if (keep) {
                tempOldName = oldName.getName();
            }
            if (askForInput) {
                String rs = Dialogs.askForInput(oldName.getName(), "Enter a new name:");
                if (!rs.equals(Dialogs.NO_INPUT))
                    tempOldName = rs;
            }
            File newFile = new File(
                    dirPath + "\\" + prefix + num + tempOldName + postfix + "." + getExtension(oldName.getName()));
            try {
                oldName.renameTo(newFile);
            } catch (Exception e) {
                return new Message(Message.ERROR_TYPE, "Renaming failed: " + oldName.getName() + "\n" + e.getMessage(),
                        Message.ERROR_COLOR);
            }
            return new Message(Message.SUCCESS_TYPE, "Succeeded", Message.SUCCESS_COLOR);
        }
        return new Message(Message.NORMAL_TYPE, "Didn't match", Message.NORMAL_COLOR);
    }

    public static String toCodeNumber(String codeFormat, int num) {
        return new DecimalFormat(codeFormat).format(num);
    }

    public static boolean checkIfExist(String path) {
        try {
            return path != null &&
                    path.length() != 0 &&
                    Files.exists(Path.of(path), LinkOption.NOFOLLOW_LINKS);
        } catch (Exception e) {
            return false;
        }
    }

    public static String getExtension(String fileName) {
        String[] exten = { "" };
        if (fileName.contains("."))
            exten = fileName.split(Pattern.quote("."));
        return exten[exten.length - 1];
    }

    public static File[] getFiles(String dirPath) {
        return new File(dirPath).listFiles();
    }

}
