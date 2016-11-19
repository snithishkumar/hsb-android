package com.archide.hsb.view.activities;

        import android.content.Context;
        import android.graphics.Bitmap;
        import android.graphics.Matrix;
        import android.media.ExifInterface;


        import java.io.ByteArrayOutputStream;
        import java.io.File;
        import java.io.FileInputStream;
        import java.io.FileOutputStream;
        import java.io.IOException;
        import java.io.InputStream;
        import java.io.InputStreamReader;
        import java.io.OutputStream;
        import java.io.Reader;
        import java.io.StringWriter;
        import java.io.Writer;
        import java.util.UUID;
        import java.util.zip.ZipEntry;
        import java.util.zip.ZipInputStream;


/**
 * Created by ram on 11/11/2014.
 */
public class FileUtils {

    private static final int DEFAULT_BUFFER_SIZE = 1024 * 4;

    public static void createFile(String path, String data, String fileName) {
        FileOutputStream fileOutputStream = null;
        try {

            File folder = new File(path);
            folder.mkdirs();
            File file = new File(path + File.separator + fileName);
            file.createNewFile();
            fileOutputStream = new FileOutputStream(file);
            write(data, fileOutputStream);
            fileOutputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
    }

    public static void addFilestoNotes(String path, String guid, String data) {
        FileOutputStream fileOutputStream = null;
        try {
            File folder = new File(path);
            folder.mkdirs();
            File file = new File(path + File.separator + guid + ".json");
            file.createNewFile();
            fileOutputStream = new FileOutputStream(file);
            write(data, fileOutputStream);
            fileOutputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }

    }



    public static void deleteFile(File file) {
        if (file.exists()) {
            file.delete();
        }
    }


    public static void deleteFolder(String path) {
        try {
            File file = new File(path);
            if (file.exists()) {
                file.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * delete particular folder
     *
     * @param file
     * @throws IOException
     */
    public static void deleteFolderRecursively(File file)
            throws IOException {

        if (file.isDirectory()) {

            //directory is empty, then delete it
            if (file.list().length == 0) {

                file.delete();
                System.out.println("Directory is deleted : "
                        + file.getAbsolutePath());

            } else {

                //list all the directory contents
                String files[] = file.list();

                for (String temp : files) {
                    //construct the file structure
                    File fileDelete = new File(file, temp);

                    //recursive delete
                    deleteFolderRecursively(fileDelete);
                }

                //check the directory again, if empty then delete it
                if (file.list().length == 0) {
                    file.delete();
                    System.out.println("Directory is deleted : "
                            + file.getAbsolutePath());
                }
            }

        } else {
            //if file, then delete it
            file.delete();
            System.out.println("File is deleted : " + file.getAbsolutePath());
        }
    }

    /**
     * Create file in cache directory and stream data is written in this file.
     *
     * @param data
     * @return
     */
    public static File downloadData(byte[] data, Context context, String fileName) {
        FileOutputStream fileOutputStream = null;
        File file = null;
        try {

            String path = DiskCacheUtils.getDiskCacheDir(context, UUID.randomUUID().toString(), "hsb");
            file = new File(path);
            file.mkdirs();
            path = path + File.separator + fileName;
            file = new File(path);
            file.createNewFile();
            fileOutputStream = new FileOutputStream(file);


            write(data, fileOutputStream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.flush();
                    fileOutputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
        return file;
    }


    /**
     * Create file in cache directory and stream data is written in this file.
     *
     * @param inputStream
     * @return
     */
    public static File downloadData(InputStream inputStream, Context context, String fileName, boolean isGeoFile, boolean isPdfNotebook, long size) {
        FileOutputStream fileOutputStream = null;
        File file = null;
        try {
            String path = null;
            if (isGeoFile) {
                path = FileUtils.getPath(context, "geoFiles");
            } else {
                path = DiskCacheUtils.getDiskCacheDir(context, UUID.randomUUID().toString(), "hsb");
            }

            file = new File(path);
            file.mkdirs();
            path = path + File.separator + fileName;
            file = new File(path);
            file.createNewFile();
            fileOutputStream = new FileOutputStream(file);

            copy(inputStream, fileOutputStream, isPdfNotebook, size);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.flush();
                    fileOutputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
        return file;
    }


    /**
     * Create file in External/Internal directory and stream Map data is written in this file.
     *
     * @param inputStream
     * @param context
     * @param fileName
     * @return String
     */
    public static String downloadMapData(InputStream inputStream, Context context, String fileName) {
        String path = null;
        try {
            path = getMapPath(context, fileName);
            downloadStreamData(inputStream, path);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return path;
    }


    /**
     * Create file in External/Internal directory and stream Resource data is written in this file.
     *
     * @param inputStream
     * @param context
     * @param fileName
     * @return String
     */
    public static String downloadResourceData(InputStream inputStream, Context context, String fileName) {
        String path = null;
        try {
            path = getResourcePath(context, fileName);
            downloadStreamData(inputStream, path);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return path;
    }


    /**
     * Create file in External/Internal directory and stream data is written in this file.
     *
     * @param inputStream
     * @param pathWithName
     */
    public static void downloadStreamData(InputStream inputStream, String pathWithName) throws Exception {
        FileOutputStream fileOutputStream = null;

        try {

            fileOutputStream = new FileOutputStream(pathWithName);
            copy(inputStream, fileOutputStream, false, 0);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e);
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.flush();
                    fileOutputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
    }

    public static Bitmap rotateBitmap(Bitmap bitmap, int orientation) {

        Matrix matrix = new Matrix();
        switch (orientation) {
            case ExifInterface.ORIENTATION_NORMAL:
                return bitmap;
            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                matrix.setScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.setRotate(180);
                break;
            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                matrix.setRotate(180);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_TRANSPOSE:
                matrix.setRotate(90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.setRotate(90);
                break;
            case ExifInterface.ORIENTATION_TRANSVERSE:
                matrix.setRotate(-90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.setRotate(-90);
                break;
            default:
                return bitmap;
        }
        try {
            Bitmap bmRotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            bitmap.recycle();
            return bmRotated;
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getMapPath(Context context, String fileName) {
        return getPath(context, "maps") + File.separator + fileName;
    }


    public static String getResourcePath(Context context, String fileName) {
        return getPath(context, "resource") + File.separator + fileName;
    }


    public static String getPath(Context context, String folderName) {
        String appRootFolder = DiskCacheUtils.getAppRootPath(context);
        File file = new File(appRootFolder + File.separator + folderName);
        if (!file.exists()) {
           boolean test = file.mkdirs();
            System.out.println(test);
        }
        return file.getPath();
    }

    public static void createDir(String path) {
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
    }



    /**
     * Get fileName from the Absolute Path
     *
     * @param path
     * @return
     */
    public static String getResourceName(String path) {
        int startAt = path.lastIndexOf("/") + 1;
        int endAt = path.length();
        if (startAt < endAt) {
            return path.substring(startAt, endAt);
        }
        return path;


    }


    /**
     * Get fileName from the Absolute Path
     *
     * @param path
     * @return
     */
    public static String getResourceNameNoExt(String path) {
        int startAt = path.lastIndexOf("/") + 1;
        int endAt = path.lastIndexOf(".");
        if (startAt < endAt) {
            return path.substring(startAt, endAt);
        }
        return "";


    }


    public static String getExtension(String path) {
        int startAt = path.lastIndexOf(".") + 1;
        int endAt = path.length();
        if (startAt < endAt) {
            return path.substring(startAt, endAt);
        }
        return "";
    }


    public static long copyLarge(InputStream input, OutputStream output)
            throws IOException {
        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
        long count = 0;
        int n = 0;
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
            count += n;
        }
        return count;
    }

    public static long downloadProgresscopyLarge(InputStream input, OutputStream output, long size)
            throws IOException {
        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
        long count = 0;
        int n = 0;
        while (-1 != (n = input.read(buffer))) {
            count += n;
            double currentBytes = (double) count;
            double totalBytes = (double) size;
            int percent = (int) ((currentBytes / totalBytes) * 100);
            output.write(buffer, 0, n);
           // GeoConstants.postPdfProgressValue(true, 3, percent, false);
        }
        return count;
    }


    public static long copyLarge(Reader input, Writer output)
            throws IOException {
        char[] buffer = new char[DEFAULT_BUFFER_SIZE];
        long count = 0;
        int n = 0;
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
            count += n;
        }
        return count;
    }


    public static int copy(InputStream input, OutputStream output, boolean downloadProgress, long size) throws IOException {
        long count;
        if (downloadProgress) {
            count = downloadProgresscopyLarge(input, output, size);
        } else {
            count = copyLarge(input, output);
        }
        if (count > Integer.MAX_VALUE) {
            return -1;
        }
        return (int) count;
    }


    public static int copy(InputStream input, Writer output) throws IOException {
        InputStreamReader in = new InputStreamReader(input);
        long count = copyLarge(in, output);
        if (count > Integer.MAX_VALUE) {
            return -1;
        }
        return (int) count;
    }


    public static void write(byte[] data, OutputStream output)
            throws IOException {
        if (data != null) {
            output.write(data);
        }
    }

    public static void write(String data, OutputStream output) throws IOException {
        if (data != null) {
            output.write(data.getBytes());
        }
    }

    public static String toString(InputStream input) throws IOException {
        StringWriter sw = new StringWriter();
        copy(input, sw);
        return sw.toString();
    }

    public static void checkFolder(String path) {
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    public static boolean isGeoFileProgress(Context context) {
        String geoPath = getPath(context, "geoFiles");
        boolean isGeoFile = false;
        File file = new File(geoPath);
        if (file != null) {
            if (file.listFiles().length > 0) {
                isGeoFile = true;
            }
        }
        return isGeoFile;
    }


    /**
     * unzip zip file
     * @param inputStream
     * @param outputFolder
     */
    public static void unZipFile(InputStream inputStream, String outputFolder) {
        byte[] buffer = new byte[1024];
        try {
            //create output directory is not exists
            File folder = new File(outputFolder);
            if (!folder.exists()) {
                folder.mkdir();
            }
            //get the zip file content
            ZipInputStream zis =
                    new ZipInputStream(inputStream);
            //get the zipped file list entry
            ZipEntry ze = zis.getNextEntry();
            while (ze != null) {
                String fileName = ze.getName();
                File newFile = new File(outputFolder + File.separator + fileName);
                System.out.println("file unzip : " + newFile.getAbsoluteFile());
                //create all non exists folders
                //else you will hit FileNotFoundException for compressed folder
                new File(newFile.getParent()).mkdirs();
                FileOutputStream fos = new FileOutputStream(newFile);
                int len;
                while ((len = zis.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }

                fos.close();
                ze = zis.getNextEntry();
            }
            zis.closeEntry();
            zis.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    public static File getGeoFileFromFolder(File folder, String fileName){
        File[] files = folder.listFiles();
        if(files != null) {
            for (File file1 : files) {
                if(file1.isDirectory()){
                    File[] folderFiles = file1.listFiles();
                    for(File file2 : folderFiles){
                        if(file2.getName().equals(fileName)){
                            return file2;
                        }
                    }
                }else{
                    if(file1.getName().equals(fileName)){
                        return file1;
                    }
                }
            }
        }
        return null;
    }
    public static File getFileFromFolder(String fileName,File file){
        File[] files = file.listFiles();
        if(files != null){
            for(File file1 : files){
                if(file1.getName().equals(fileName)){
                    return file1;
                }
            }
        }
        return null;
    }

    /**
     * Copy the file from Src path to dest Path
     *
     * @param srcPath
     * @param resourcePath
     */
    public static void addResources(String srcPath, String resourcePath) {
        File srcFrile = new File(srcPath);
        if (srcFrile.exists()) {
            FileInputStream fileInputStream = null;
            FileOutputStream fileOutputStream = null;
            try {
                fileInputStream = new FileInputStream(srcFrile);
                fileOutputStream = new FileOutputStream(resourcePath);
                FileUtils.copy(fileInputStream, fileOutputStream, false, 0);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (fileOutputStream != null) {
                        fileOutputStream.flush();
                        fileOutputStream.close();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    if (fileInputStream != null) {
                        fileInputStream.close();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }


    }

}