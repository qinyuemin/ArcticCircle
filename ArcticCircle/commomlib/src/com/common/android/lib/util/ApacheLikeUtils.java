/**
 *
 *项目名：LuluYouLibForLianyuan
 *包名：com.luluyou.android.lib.utils
 *文件名：ApacheLikeUtils.java
 *版本信息：1.0.0
 *创建日期：2014-6-22-下午5:14:07
 *创建人：jerry.deng
 *Copyright (c) 2014上海路路由信息科技有限公司-版权所有
 * 
 */
package com.common.android.lib.util;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URLConnection;
import java.nio.channels.FileChannel;

/**
 *
 * 类名称：ApacheLikeUtils
 * 类描述：
 * 创建人： Jerry.deng
 * 修改人： Jerry.deng
 * 修改时间： 2014-6-22 下午5:14:07
 * 修改备注：
 * @version 1.0.0
 *
 */
public class ApacheLikeUtils {
	
	private static final int EOF = -1;
	private static final int DEFAULT_BUFFER_SIZE = 1024*4;
	 public static final long ONE_KB = 1024;
	 public static final long ONE_MB = ONE_KB * ONE_KB;
	private static final long FILE_COPY_BUFFER_SIZE = ONE_MB * 30;
	public static String join( String[] array, String separator )
	{
		StringBuffer buf = new StringBuffer();
		for( int i = 0; i < array.length; i++ )
		{
			if( i > 0 )
				buf.append( separator );
			buf.append( array[i] );
		}
		return buf.toString();
	}
	
	public static int copy(InputStream input, OutputStream output) throws IOException {
        long count = copyLarge(input, output);
        if (count > Integer.MAX_VALUE) {
            return -1;
        }
        return (int) count;
    }
	
	
	private static long copyLarge(InputStream input, OutputStream output)
            throws IOException {
        long count = 0;
        int n = 0;
        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
        while (EOF != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
            count += n;
        }
        return count;
    }
	
	public static <T> T notNull(final T object, final String message, final Object... values) {
        if (object == null) {
            throw new NullPointerException(String.format(message, values));
        }
        return object;
    }
	
	public static void copyFile(File srcFile, File destFile,
            boolean preserveFileDate) throws IOException {
        if (srcFile == null) {
            throw new NullPointerException("Source must not be null");
        }
        if (destFile == null) {
            throw new NullPointerException("Destination must not be null");
        }
        if (srcFile.exists() == false) {
            throw new FileNotFoundException("Source '" + srcFile + "' does not exist");
        }
        if (srcFile.isDirectory()) {
            throw new IOException("Source '" + srcFile + "' exists but is a directory");
        }
        if (srcFile.getCanonicalPath().equals(destFile.getCanonicalPath())) {
            throw new IOException("Source '" + srcFile + "' and destination '" + destFile + "' are the same");
        }
        File parentFile = destFile.getParentFile();
        if (parentFile != null) {
            if (!parentFile.mkdirs() && !parentFile.isDirectory()) {
                throw new IOException("Destination '" + parentFile + "' directory cannot be created");
            }
        }
        if (destFile.exists() && destFile.canWrite() == false) {
            throw new IOException("Destination '" + destFile + "' exists but is read-only");
        }
        doCopyFile(srcFile, destFile, preserveFileDate);
    }
	
	private static void doCopyFile(File srcFile, File destFile, boolean preserveFileDate) throws IOException {
        if (destFile.exists() && destFile.isDirectory()) {
            throw new IOException("Destination '" + destFile + "' exists but is a directory");
        }

        FileInputStream fis = null;
        FileOutputStream fos = null;
        FileChannel input = null;
        FileChannel output = null;
        try {
            fis = new FileInputStream(srcFile);
            fos = new FileOutputStream(destFile);
            input  = fis.getChannel();
            output = fos.getChannel();
            long size = input.size();
            long pos = 0;
            long count = 0;
            while (pos < size) {
                count = size - pos > FILE_COPY_BUFFER_SIZE ? FILE_COPY_BUFFER_SIZE : size - pos;
                pos += output.transferFrom(input, pos, count);
            }
        } finally {
        	
            closeQuietly(output);
            closeQuietly(fos);
            closeQuietly(input);
            closeQuietly(fis);
        }

        if (srcFile.length() != destFile.length()) {
            throw new IOException("Failed to copy full contents from '" +
                    srcFile + "' to '" + destFile + "'");
        }
        if (preserveFileDate) {
            destFile.setLastModified(srcFile.lastModified());
        }
    }
	
	public static void close(URLConnection conn) {
        if (conn instanceof HttpURLConnection) {
            ((HttpURLConnection) conn).disconnect();
        }
    }
	
	public static void closeQuietly(Reader input) {
        closeQuietly((Closeable)input);
    }
	
	public static void closeQuietly(Writer output) {
        closeQuietly((Closeable)output);
    }
	
	public static void closeQuietly(OutputStream output) {
        closeQuietly((Closeable)output);
    }
	
	public static void closeQuietly(InputStream input) {
        closeQuietly((Closeable)input);
    }
	
	public static void closeQuietly(Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (IOException ioe) {
            // ignore
        }
    }
	
}
