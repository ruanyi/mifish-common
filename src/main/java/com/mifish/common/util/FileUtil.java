package com.mifish.common.util;

import com.google.common.collect.Lists;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * FileUtil
 *
 * @author rls
 * @time:2013-07-18
 */
public final class FileUtil extends FileUtils {

    /**
     * getSimilarFile
     *
     * @param directory
     * @param prefix
     * @return
     */
    public static List<File> getSimilarFile(String directory, String prefix) {
        return getSimilarFile(directory, prefix, false);
    }

    /**
     * getSimilarFile
     *
     * @param directory
     * @param prefix
     * @param asc
     * @return
     */
    public static List<File> getSimilarFile(String directory, String prefix, final boolean asc) {
        if (StringUtils.isBlank(directory)) {
            return Lists.newArrayList();
        }
        File dir = new File(directory);
        if (!dir.exists() || !dir.isDirectory()) {
            return Lists.newArrayList();
        }
        boolean isFilter = StringUtils.isNotBlank(prefix);
        File[] files = dir.listFiles((d, name) -> {
            return isFilter ? name.startsWith(prefix) : true;
        });
        List<File> filelist = Arrays.asList(files);
        Collections.sort(filelist, (file1, file2) -> {
            long fm1 = ((file1 == null) ? 0 : file1.lastModified());
            long fm2 = ((file2 == null) ? 0 : file2.lastModified());
            if (fm1 == fm2) {
                return 0;
            }
            return asc ? ((fm1 > fm2) ? 1 : -1) : ((fm1 > fm2) ? -1 : 1);
        });
        return filelist;
    }

    /**
     * readFileBytes
     *
     * @param filepath
     * @return
     */
    public static byte[] readFileBytes(String filepath) {
        if (StringUtil.isBlank(filepath)) {
            return null;
        }
        //readFileBytes
        return readFileBytes(new File(filepath));
    }

    /**
     * readFileBytes
     *
     * @param file
     * @return
     */
    public static byte[] readFileBytes(File file) {
        checkArgument(file != null, "FileUtil,readFileBytes,File cannot be null");
        FileInputStream fis = null;
        FileChannel fc = null;
        try {
            fis = new FileInputStream(file);
            fc = fis.getChannel();
            ByteBuffer dest = ByteBuffer.allocate((int) fc.size());
            while (true) {
                if (fc.read(dest) <= 0) {
                    break;
                }
            }
            return dest.array();
        } catch (IOException ex) {
            throw new RuntimeException("FileUtil,readFileBytes,IOException,FilePath[" + file.getPath() + "]", ex);
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    //ignore
                }
            }
            if (fc != null) {
                try {
                    fc.close();
                } catch (IOException e) {
                    //ignore
                }
            }
        }
    }

    /**
     * FileUtil
     *
     * @throws Exception
     */
    private FileUtil() throws Exception {
        throw new IllegalAccessException("FileUtil cannot be init");
    }
}
