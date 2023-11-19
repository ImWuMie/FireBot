package dev.wumie.system.event.impl;

import dev.wumie.messages.QMessage;
import dev.wumie.system.actions.PicAction;
import dev.wumie.system.event.MsgEvent;
import dev.wumie.system.user.UserInfo;
import dev.wumie.utils.RandomUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ImageSaverEvent extends MsgEvent {
    public ImageSaverEvent() {
        super("image_saver");
    }

    private final File[] lastImages = new File[10];
    private int fileIndex = 0;

    private long lastSend = 0;
    private long messageCooldown = 0;

    @Override
    public void run(String message, QMessage exec, UserInfo userInfo) {
        // 随机图片发送
        int id = RandomUtils.nextInt(0, lastImages.length - 1);
        if (lastImages[id] != null) {
            if (System.currentTimeMillis() - messageCooldown <= 250) {
                if (System.currentTimeMillis() - lastSend >= 5000) {
                    exec.send(new PicAction(lastImages[id]).toAction());
                    lastSend = System.currentTimeMillis();
                }
            }
        }

        try {
            if (isCQCode(message)) {
                String type = getCQType(message);
                if (type.equalsIgnoreCase("image")) {
                    int index = "[CQ:image,file=".length();
                    String file = message.substring(index, message.indexOf(",", index));
                    int subTypeIndex = ("[CQ:image,file=" + file + ",subType=").length();
                    String subType = message.substring(subTypeIndex, message.indexOf(",", subTypeIndex));
                    if (Integer.parseInt(subType) != 1) return;

                    int urlIndex = ("[CQ:image,file=" + file + ",subType=" + subType + ",url=").length();
                    String url = message.substring(urlIndex, message.length() - 1);
                    File imageFile = new File(FOLDER, file);
                    if (!imageFile.exists()) {
                        try {
                            //exec.send("好图,我的了");
                            downloadImage(url, imageFile);
                            lastImages[fileIndex] = imageFile;
                            fileIndex++;
                            if (fileIndex >= lastImages.length) {
                                fileIndex = 0;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            imageFile.delete();
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        messageCooldown = System.currentTimeMillis();
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
