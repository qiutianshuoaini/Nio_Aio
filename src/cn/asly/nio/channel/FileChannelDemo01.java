package cn.asly.nio.channel;


import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class FileChannelDemo01 {
    public static void main(String[] args) throws IOException {
        FileInputStream fis = new FileInputStream("H:\\作业\\就业班\\day19【JUnit单元测试、NIO】\\笔记.md");
        FileOutputStream fos = new FileOutputStream("copy.md");

        FileChannel channelIn = fis.getChannel();//用来读取源文件
        FileChannel channelOut = fos.getChannel();//用来写入目标文件

        ByteBuffer buffer = ByteBuffer.allocate(1024);
        while (channelIn.read(buffer) != -1) {
            buffer.flip();//切换为读状态
            channelOut.write(buffer);//读取缓冲区数据，写入到文件中
            buffer.clear();
        }
        channelOut.close();
        channelIn.close();
        fos.close();
        fis.close();


    }
}