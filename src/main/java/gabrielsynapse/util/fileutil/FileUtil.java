package gabrielsynapse.util.fileutil;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class FileUtil {

    public static boolean renameFile(String str, String str2) {
        return new File(str).renameTo(new File(str2));
    }

    /**
     * @return A filename without its extension,
     * e.g. "FileUtil" for "FileUtil.java", or "FileUtil" for "/sdcard/Documents/FileUtil.java"
     */
    public static String getFileNameNoExtension(String filePath) {
        if (filePath.trim().isEmpty()) return "";

        int lastPos = filePath.lastIndexOf('.');
        int lastSep = filePath.lastIndexOf(File.separator);

        if (lastSep == -1) {
            return (lastPos == -1 ? filePath : filePath.substring(0, lastPos));
        } else if (lastPos == -1 || lastSep > lastPos) {
            return filePath.substring(lastSep + 1);
        }
        return filePath.substring(lastSep + 1, lastPos);
    }

    /**
     * @return A file's filename extension,
     * e.g. "java" for "/sdcard/Documents/FileUtil.java", but "" for "/sdcard/Documents/fileWithoutExtension"
     */
    public static String getFileExtension(String filePath) {
        if (filePath.isEmpty()) return "";

        int last = filePath.lastIndexOf('.');
        int lastSep = filePath.lastIndexOf(File.separator);

        if (last == -1 || lastSep >= last) return "";
        return filePath.substring(last + 1);
    }

    private static void createNewFileIfNotPresent(String path) {
        int lastSep = path.lastIndexOf(File.separator);
        if (lastSep > 0) {
            String dirPath = path.substring(0, lastSep);
            makeDir(dirPath);
        }

        File file = new File(path);

        try {
            if (!file.exists()) file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String readFile(String path) {
        createNewFileIfNotPresent(path);

        StringBuilder sb = new StringBuilder();
        try (FileReader fr = new FileReader(path)) {
            char[] buff = new char[1024];
            int length;

            while ((length = fr.read(buff)) > 0) {
                sb.append(new String(buff, 0, length));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    public static void writeFile(String path, String str) {
        createNewFileIfNotPresent(path);

        try (FileWriter fileWriter = new FileWriter(path, false)) {
            fileWriter.write(str);
            fileWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void copyFile(String sourcePath, String destPath) {
        if (!isExistFile(sourcePath)) return;
        createNewFileIfNotPresent(destPath);

        try (FileInputStream fis = new FileInputStream(sourcePath);
             FileOutputStream fos = new FileOutputStream(destPath, false)) {
            byte[] buffer = new byte[1024];
            int length;

            while ((length = fis.read(buffer)) > 0) {
                fos.write(buffer, 0, length);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Copies an entire directory, recursively.
     *
     * @param source   The directory whose contents to copy.
     * @param copyInto The directory to copy files into.
     * @throws IOException Thrown when something goes wrong while copying.
     */
    public static void copyDirectory(File source, File copyInto) throws IOException {
        if (!source.isDirectory()) {
            File parentFile = copyInto.getParentFile();
            if (parentFile == null || parentFile.exists() || parentFile.mkdirs()) {
                FileInputStream fileInputStream = new FileInputStream(source);
                FileOutputStream fileOutputStream = new FileOutputStream(copyInto);
                byte[] bArr = new byte[2048];
                while (true) {
                    int read = fileInputStream.read(bArr);
                    if (read <= 0) {
                        fileInputStream.close();
                        fileOutputStream.close();
                        return;
                    }
                    fileOutputStream.write(bArr, 0, read);
                }
            } else {
                throw new IOException("Cannot create dir " + parentFile.getAbsolutePath());
            }
        } else if (copyInto.exists() || copyInto.mkdirs()) {
            String[] list = source.list();
            if (list != null) {
                for (String s : list) {
                    copyDirectory(new File(source, s), new File(copyInto, s));
                }
            }
        } else {
            throw new IOException("Cannot create dir " + copyInto.getAbsolutePath());
        }
    }

    public static void extractFileFromZip(InputStream inputStream, File file) throws IOException {
        try (OutputStream outputStream = new FileOutputStream(file)) {
            byte[] bArr = new byte[1024];
            while (true) {
                int read = inputStream.read(bArr);
                if (read > 0) {
                    outputStream.write(bArr, 0, read);
                } else {
                    return;
                }
            }
        }
    }

    public static void moveFile(String sourcePath, String destPath) {
        copyFile(sourcePath, destPath);
        deleteFile(sourcePath);
    }

    public static void deleteFile(String path) {
        File file = new File(path);

        if (!file.exists()) return;

        if (file.isFile()) {
            file.delete();
            return;
        }

        File[] fileArr = file.listFiles();

        if (fileArr != null) {
            for (File subFile : fileArr) {
                if (subFile.isDirectory()) {
                    deleteFile(subFile.getAbsolutePath());
                }

                if (subFile.isFile()) {
                    subFile.delete();
                }
            }
        }

        file.delete();
    }

    public static boolean isExistFile(String path) {
        return new File(path).exists();
    }

    public static void makeDir(String path) {
        if (!isExistFile(path)) {
            new File(path).mkdirs();
        }
    }

    public static void listDir(String path, ArrayList<String> list) {
        File[] listFiles;
        File dir = new File(path);
        if (!(!dir.exists() || dir.isFile() || (listFiles = dir.listFiles()) == null || listFiles.length <= 0 || list == null)) {
            list.clear();
            for (File file : listFiles) {
                list.add(file.getAbsolutePath());
            }
        }
    }

    /**
     * @return List of files that have the filename extension {@code extension}.
     */
    public static ArrayList<String> listFiles(String dir, String extension) {
        ArrayList<String> list = new ArrayList<>();
        ArrayList<String> files = new ArrayList<>();
        listDir(dir, files);
        for (String str : files) {
            if (str.endsWith(extension) && isFile(str)) {
                list.add(str);
            }
        }
        return list;
    }

    public static boolean isDirectory(String path) {
        if (!isExistFile(path)) {
            return false;
        }
        return new File(path).isDirectory();
    }

    public static boolean isFile(String path) {
        if (!isExistFile(path)) {
            return false;
        }
        return new File(path).isFile();
    }

    public static long getFileLength(String path) {
        if (!isExistFile(path)) {
            return 0;
        }
        return new File(path).length();
    }


    public static byte[] readFromInputStream(InputStream stream) {
        int available;

        try {
            available = stream.available();
        } catch (IOException e) {
            available = 0;
        }

        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[available];

        try {
            for (int len = stream.read(buffer); len != -1; len = stream.read(buffer)) {
                outputStream.write(buffer, 0, len);
            }
        } catch (IOException e) {
            return new byte[0];
        }

        return outputStream.toByteArray();
    }

    /**
     * Write bytes to a file.
     *
     * @param target The file to write the data to. Note that it'll get created, even parent directories
     * @param data   The data in bytes to write to. {@link FileUtil#readFromInputStream(InputStream)}
     *               for example, reads bytes
     * @throws IOException Thrown when any exception occurs while operating
     */
    public static void writeBytes(File target, byte[] data) throws IOException {
        if (!target.exists()) {
            target.getParentFile().mkdirs();
        }
        BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(target));
        outputStream.write(data);
        outputStream.flush();
        outputStream.close();
    }

    public static void extractZipTo(ZipInputStream input, String outPath) throws IOException {
        File outDir = new File(outPath);
        if (!outDir.exists()) {
            outDir.mkdirs();
        }

        ZipEntry entry = input.getNextEntry();
        while (entry != null) {
            String entryPathExtracted = new File(outPath, entry.getName()).getAbsolutePath();

            if (!entry.isDirectory()) {
                new File(entryPathExtracted).getParentFile().mkdirs();
                writeBytes(new File(entryPathExtracted), readFromInputStream(input));
            }
            input.closeEntry();
            entry = input.getNextEntry();
        }
        input.close();
    }
}