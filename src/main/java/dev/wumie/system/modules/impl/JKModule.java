package dev.wumie.system.modules.impl;

import dev.wumie.FireQQ;
import dev.wumie.messages.QMessage;
import dev.wumie.system.actions.PicAction;
import dev.wumie.system.modules.Module;
import dev.wumie.system.user.UserInfo;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class JKModule extends Module {
    public JKModule() {
        super("jk", "jb");
    }

    private long lastSend = 0;
    private long lastClear = System.currentTimeMillis();

    @Override
    public void run(String[] args, QMessage message, UserInfo userInfo) {
        if (System.currentTimeMillis() - lastClear >= 60000L) {
            for (int j = 0; j < 30; j++) {
                File file = new File(FOLDER, "jk" + j + ".png");
                if (file.exists()) {
                    file.delete();
                }
            }

            lastClear = System.currentTimeMillis();
        }

        FireQQ.INSTANCE.async.submit(() -> {
            if (System.currentTimeMillis() - lastSend >= 5000L) {

                File file = new File(FOLDER, "jk0.png");

                for (int j = 0; j < 30; j++) {
                    file = new File(FOLDER, "jk" + j + ".png");
                    if (!file.exists()) {
                        break;
                    }
                }

                try {
                    downloadImage("https://api.8uid.cn/api/jk.php", file);
                    String cq = new PicAction(file).toAction();
                    message.send(cq);
                    lastSend = System.currentTimeMillis();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void downloadImage(String i, File to) throws Exception {
        //Files.copy(Http.get(i).sendInputStream(), to.toPath());
        //定义一个URL对象，就是你想下载的图片的URL地址
        URL url = new URL(i);
        //打开连接
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        //设置请求方式为"GET"
        conn.setRequestMethod("GET");
        //超时响应时间为10秒
        conn.setConnectTimeout(10 * 1000);
        InputStream is = conn.getInputStream();
        byte[] data = readInputStream(is);
        FileOutputStream outStream = new FileOutputStream(to);
        //写入数据
        outStream.write(data);
        //关闭输出流，释放资源
        outStream.close();
    }

    public byte[] readInputStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        //创建一个Buffer字符串
        byte[] buffer = new byte[1024 * 1024 * 30];
        //每次读取的字符串长度，如果为-1，代表全部读取完毕
        int len;
        //使用一个输入流从buffer里把数据读取出来
        while ((len = inStream.read(buffer)) != -1) {
            //用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度
            outStream.write(buffer, 0, len);
        }
        //关闭输入流
        inStream.close();
        //把outStream里的数据写入内存
        return outStream.toByteArray();
    }
}
