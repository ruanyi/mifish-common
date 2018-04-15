package com.mifish.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * ZipUtil
 *
 * @author rls
 * @time:2013-07-27
 */
public final class ZipUtil {

    /**
     * zipFolder
     *
     * @param folderPath
     * @param zipfile
     * @throws IOException
     */
    public static void zipFolder(String folderPath, String zipfile) throws IOException {
        File zipfolder = new File(folderPath);
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipfile));
        try {
            zipFile(out, zipfolder, zipfolder.getName());
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    /**
     * zipFile
     *
     * @param out
     * @param file
     * @param base
     * @throws IOException
     */
    private static void zipFile(ZipOutputStream out, File file, String base) throws IOException {
        if (file.isDirectory()) {
            File[] filelist = file.listFiles();
            for (File f : filelist) {
                zipFile(out, f, base + File.separator + f.getName());
            }
        } else {
            out.putNextEntry(new ZipEntry(base));
            FileInputStream in = new FileInputStream(file);
            try {
                int b = 0;
                while ((b = in.read()) != -1) {
                    out.write(b);
                }
                out.flush();
            } finally {
                if (in != null) {
                    in.close();
                }
            }
        }
    }

    /**
     * 解压zip文件，
     * <p>
     *
     * @param fileName
     * @param outputDir
     * @throws Exception
     */
    public static void unzip(String fileName, String outputDir) throws Exception {
        FileInputStream fis = null;
        ZipInputStream zipIs = null;
        byte[] buffer = new byte[1024];
        try {
            fis = new FileInputStream(fileName);
            zipIs = new ZipInputStream(fis);
            FileOutputStream fos = null;
            ZipEntry zipEntry = null;
            while ((zipEntry = zipIs.getNextEntry()) != null) {
                if (zipEntry.isDirectory()) {
                    continue;
                }
                String name = zipEntry.getName();
                File newFile = new File(outputDir, name);
                if (!newFile.getParentFile().exists()) {
                    //创建文件父目录
                    newFile.getParentFile().mkdirs();
                }
                fos = new FileOutputStream(newFile);
                int length;
                while ((length = zipIs.read(buffer)) > 0) {
                    fos.write(buffer, 0, length);
                }
                fos.flush();
                fos.close();
            }
        } catch (Exception ex) {
            throw ex;
        } finally {
            if (fis != null) {
                fis.close();
            }
            if (zipIs != null) {
                zipIs.close();
            }
        }
    }

    /***ZipUtil*/
    private ZipUtil() throws Exception {
        throw new IllegalAccessException("ZipUtil cannot be init");
    }
}
