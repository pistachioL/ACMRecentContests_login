package team.huoguo.login.common.utils;

import org.apache.commons.io.FileUtils;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * 文件工具类
 * @author GreenHatHG
 **/
public class FileHandleUtil {
    /** 绝对路径 **/
    private static String absolutePath = "";

    /** 文件存放的目录 **/
    private static String fileDir = "/avatar/";

    public static void saveAvatarFromUrl(String smmsUrl, String id) throws IOException {
            URL url = new URL(smmsUrl);
            InputStream is=url.openStream();
            saveAvatar(is, id + ".png");
    }

    public static void saveAvatar(InputStream inputStream, String filename) {
        //第一次会创建文件夹
        createDirIfNotExists();

        String resultPath = fileDir + filename;

        //存文件
        File file = new File(absolutePath, resultPath);
        try {
            FileUtils.copyInputStreamToFile(inputStream, file);
        } catch (IOException e) {
            throw new RuntimeException("保存文件失败");
        }
    }

    /**
     * 创建文件夹路径
     */
    private static void createDirIfNotExists() {
        if (!absolutePath.isEmpty()) {return;}

        //获取跟目录
        File file = null;
        try {
            file = new File(ResourceUtils.getURL("classpath:").getPath());
        } catch (FileNotFoundException e) {
            throw new RuntimeException("获取根目录失败，无法创建目录！");
        }
        if(!file.exists()) {
            file = new File("");
        }

        absolutePath = file.getAbsolutePath();

        File savePath = new File(absolutePath, fileDir);
        if(!savePath.exists()) {
            savePath.mkdirs();
        }
    }
}
