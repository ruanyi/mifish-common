package com.mifish.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * ZipUtil
 *
 * @author rls
 * @time:2013-07-27
 */
public final class ZipUtil {

    /***ZipUtil*/
    private ZipUtil() throws Exception {
        throw new IllegalAccessException("ZipUtil cannot be init");
    }

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
}
