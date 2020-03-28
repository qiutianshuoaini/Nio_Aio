package cn.asly.nio.channel;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class SocketChannelDemo {
    public static void main(String[] args) throws IOException {
        SocketChannel socketChannel = SocketChannel.open();
        boolean connect = socketChannel.connect(new InetSocketAddress("127.0.0.1", 8888));
        if (connect) {
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            buffer.put("你好服务器，我是客户端".getBytes());
            socketChannel.write(buffer);

            ByteBuffer buffer1 = ByteBuffer.allocate(1024);
            int len = socketChannel.read(buffer);
            System.out.println(new String(buffer1.array(), 0, len));
        }

    }
}