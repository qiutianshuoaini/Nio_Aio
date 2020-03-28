package cn.asly.nio.channel;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class ServerSocketChannelDemo {
    public static void main(String[] args) throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(8888));

        serverSocketChannel.configureBlocking(false);//设置非阻塞式

        while (true) {

            SocketChannel socketChannel = serverSocketChannel.accept();
            if (socketChannel != null) {
                ByteBuffer buffer = ByteBuffer.allocate(1024);
                int len = socketChannel.read(buffer);
                buffer.flip();//切换为读模式

                System.out.println(new String(buffer.array(),0,len));//打印从客户端传过来的数据

                ByteBuffer buffer1 = ByteBuffer.allocate(1024);
                buffer1.put("我是服务器，我已经接收到你发送的消息".getBytes());
                socketChannel.write(buffer1);
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


            } else {
                System.out.println("还没有客户端连接上。。。。等待2秒");
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}