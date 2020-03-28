package cn.asly.nio.channel;

import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class FileChannelDemo02 {
    public static void main(String[] args) throws Exception {
        FileChannel channelIn = new RandomAccessFile("copy.md", "r").getChannel();//源文件，并设置为只读
        FileChannel channelOut = new RandomAccessFile("copy2.md", "rw").getChannel();//目标文件，设置为可读可写

        int size = (int) channelIn.size();
        MappedByteBuffer mapIn = channelIn.map(FileChannel.MapMode.READ_ONLY, 0, size);
        MappedByteBuffer mapOut = channelOut.map(FileChannel.MapMode.READ_WRITE, 0, size);

        for (int i = 0; i < size; i++) {
            mapOut.put(i, mapIn.get(i));
        }

        channelOut.close();
        channelIn.close();

    }
}