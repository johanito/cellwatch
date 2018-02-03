package com.example.niden.cellwatchsharing.controllers;


import android.os.Environment;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by niden on 31-Jan-18.
 */

public class Zip {
    public static final File mediaStorageDir = new File(Environment.getExternalStorageDirectory(),
            "yourAppFoler");

    public String zipper(List<String> allFiles, String zipFileName) throws IOException, ZipException {
        String timeStampOfZipFile =new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());mediaStorageDir.mkdirs();
       String zippath = mediaStorageDir.getAbsolutePath() + "/" + zipFileName+ "GG" +  ".zip";

            if (new File(zippath).exists())
            {
                new File(zippath).delete();
            }
           // new File(zipFileName).delete(); // Delete if exists

            ZipFile zipFile = new ZipFile(zippath);

        ZipParameters zipParameters = new ZipParameters();
            zipParameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
            zipParameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);
            zipParameters.setPassword("Reset");

            if (allFiles.size() > 0)
            {
                for (String fileName : allFiles)
                {

                    File file = new File(fileName);
                    zipFile.addFile(file,zipParameters);

                }
            }

        return zippath;
    }


}
