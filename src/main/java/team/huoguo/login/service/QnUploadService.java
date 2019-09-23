package team.huoguo.login.service;

import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author GreenHatHG
 **/
@Component
public class QnUploadService implements InitializingBean {

    private UploadManager uploadManager;
    private BucketManager bucketManager;
    private Auth auth;

    private final ResourceLoader resourceLoader;

    public QnUploadService(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Autowired
    public void setUploadManager(UploadManager uploadManager) {
        this.uploadManager = uploadManager;
    }

    @Autowired
    public void setBucketManager(BucketManager bucketManager) {
        this.bucketManager = bucketManager;
    }

    @Autowired
    public void setAuth(Auth auth) {
        this.auth = auth;
    }

    @Value("${qiniu.bucket}")
    private String bucket;

    /**
     * 定义七牛云上传的相关策略
     */
    private StringMap putPolicy;

    /**
     * 以文件的形式上传
     * @param path
     * @param fileName
     * @return
     * @throws QiniuException
     */
    public String uploadFile(String path, String fileName) throws QiniuException {

        File file = new File(readFileInJar(path));
        Response response = this.uploadManager.put(file, fileName, getUploadToken());
        int retry = 0;
        while (response.needRetry() && retry < 3) {
            response = this.uploadManager.put(file, fileName, getUploadToken());
            retry++;
        }
        if (response.statusCode == 200) {
            return fileName;
        }
        return "上传失败!";
    }

    /**
     * 删除七牛云上的相关文件
     * @param key
     * @return
     * @throws QiniuException
     */
    public String delete(String key) throws QiniuException {
        Response response = bucketManager.delete(this.bucket, key);
        int retry = 0;
        while (response.needRetry() && retry++ < 3) {
            response = bucketManager.delete(bucket, key);
        }
        return response.statusCode == 200 ? "删除成功!" : "删除失败!";
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.putPolicy = new StringMap();
        putPolicy.put("returnBody", "{\"key\":\"$(key)\",\"hash\":\"$(etag)\",\"bucket\":\"$(bucket)\",\"width\":$(imageInfo.width), \"height\":${imageInfo.height}}");
    }

    /**
     * 获取上传凭证
     * @return
     */
    private String getUploadToken() {
        return this.auth.uploadToken(bucket, null, 3600, putPolicy);
    }

    public String readFileInJar(String path) {
        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(resourceLoader.getResource(path).getInputStream()));
            String s;
            try {
                while ((s = in.readLine()) != null) {
                    sb.append(s);
                }
            } finally {
                in.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return sb.toString();
    }
}