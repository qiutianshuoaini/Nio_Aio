package cn.asly.nio.bytebuffer;

import java.nio.ByteBuffer;

public class ByteBufferDemo {
    public static void main(String[] args) {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        buffer.put("abcde".getBytes());
        buffer.flip();//调用flip()方法，limit会变为此时的position，并将position置0。也就是将buffer置为读状态
        for (int i = 0; i < buffer.limit(); i++) {
            System.out.println(buffer.get());
        }
        System.out.println(buffer.position());
        buffer.limit(buffer.capacity());

    }
}
