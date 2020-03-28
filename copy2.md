# day19【JUnit单元测试、NIO】

##### 反馈和复习

```java
1.我的IP(192.168.1.100)和老师(192.168.1.8)前面都差不多,就后面的不太一样
2.为什么我和同学TCP连接无法建立
    IP有外网IP和内网IP之分
复习:
1.网络编程三要素
    协议(TCP),IP地址,端口号
2.Socket类
   构造方法:
		public Socket(服务器的IP地址,服务器的端口号);
   成员方法:
		public void close();
		public OutputStream getOutputStream();//从客户端 到 服务器的流
		public InputStream getInputStream();//从服务器 到 客户端流
		
		public void shutDownOutput(); // 关闭输出流
		public void shutDownInput(); // 关闭输入流
3.ServerSocket类    
    	public ServerSocket(服务器的端口号);
		public Socket accept(); //接收连接到服务器的客户端对象

4.重点案例【TCP的双向通信】
    
```

##### 今日内容

```java
这两天的内容以了解为主【NIO,AIO】
今天内容:
1.Junit单元测测【重点,简单】
2.NIO 【了解】
    Buffer -- 缓冲区
    Channel -- 通道
    Selector -- 选择器(多路复用器)
    
```

### 第一章 Junit单元测试【重点】

##### 1.什么是单元测试

```java
a.什么叫单元测试
    i.单元,在java中一个单元可以指某个方法,或者某个类
    ii.测试,写一段代码对象单元进行测试
b.Junit是专门为单元测试提供一个第三方框架
    作用: 让一个普通方法独立运行(替代main方法)       
```

##### 2.Junit的使用步骤

- 下载

  ```java
  www.junit.org下载即可
  但是我们不需要去下载,因为开发工具中自带(IDEA)       
  ```


- 具体使用步骤

  ```java
  a.编写一个被测试类(业务类)
      /**
       * 被测试类,业务类
       */
      public class Dog {
  
          public int getSum(int a, int b, int c) {
              int sum = a + b + c;
              return sum;
          }
      }
  
  b.编写测试类
  c.在测试类使用Junit单元测试框架来测试 
      
      /**
       * 测试类
       */
      public class TestDog {
          @Test
          public void test01(){
              //测试代码
              Dog dd = new Dog();
              int sum = dd.getSum(1, 2, 3);
              System.out.println("结果为:"+sum);
          }
  
          @Test
          public void test02(){
              //测试代码
              Dog dd = new Dog();
              int sum = dd.getSum(11, 22, 33);
              System.out.println("结果为:"+sum);
          }
      }
  
  ```

- 运行Junit测试

  ```java
  在需要独立运行的方法上加上一个注解
  @Test 
  ```

##### 3.单元测试中其他四个注解【理解】

- Junit4.x

  ```java
  @Before 表示该方法会自动在"每个"@Test方法执行之前执行
  @After 表示该方法会自动在"每个"@Test方法执行之后执行
      
  @BeforeClass 表示该方法会自动在"所有"@Test方法执行之前执行
  @AfterClass 表示该方法会自动在"所有"@Test方法执行之后执行    
    
  注意: @BeforeClass和@AfterClass必须修饰静态方法
      
  /**
   * 测试类
   */
  public class TestDog {
  //    @Before
  //    public void aa(){
  //        System.out.println("aa...");
  //    }
  
  //    @After
  //    public void bb(){
  //        System.out.println("bb...");
  //    }
  
      @BeforeClass
      public static void aa(){
          System.out.println("aa...");
      }
      @AfterClass
      public static void bb(){
          System.out.println("bb...");
      }
      @Test
      public void test01(){
          //测试代码
          Dog dd = new Dog();
          int sum = dd.getSum(1, 2, 3);
          System.out.println("结果为:"+sum);
      }
      @Test
      public void test02(){
          //测试代码
          Dog dd = new Dog();
          int sum = dd.getSum(11, 22, 33);
          System.out.println("结果为:"+sum);
      }
  }
      
  ```

- Junit5.x

  ```java
  @BeforeEach: 相当于@Before
  @AfterEach: 相当于@After
  @BeforeAll: 相当于@BeforeClass
  @AfterAll: 相当于@AfterClass
  注意: @BeforeAll和@AfterAll必须修饰静态方法   
      
  讲义中有一段代码:
  	断言: Assert.assertEquals("异常信息",得到结果,预期的结果);
  	作用: 比较 "得到结果" 和 "预期的结果"
           如果一样,什么都不做,如果不一样抛出异常,并且封装异常的信息
      案例:
  		    @Test
              public void test01(){
                  //测试代码
                  Dog dd = new Dog();
                  int sum = dd.getSum(1, 2, 3);
                  //使用断言
                  Assert.assertEquals("结果不一致呀",sum,10);
              }
          
      
  ```

### 第二章 NIO介绍(了解)

##### 1.阻塞与非阻塞

```java
阻塞: 完成某个任务时,任务没有结束之前,当前线程无法向下继续执行
非阻塞: 完成某个任务时,不需要等待任务结束,当前线程立即可以继续向下执行,后期我们再通过其他代码获取任务结果 
```

##### 2.同步与异步

```java
同步: 
	同步可能是阻塞的,也可能非阻塞的
    "同步阻塞":  完成某个任务时,任务没有结束之前,当前线程无法向下执行
	"同步非阻塞": 完成某个任务时,不需要等待任务结束,当前线程立即可以继续向下执行,
									后期需要我们自己写其他代码获取结果
异步:
	异步一般来说都是非阻塞
    "异步非阻塞":  完成某个任务时,不需要等待任务结束,当前线程立即可以继续向下执行,
				后期我们不需要自己写其他代码来获取结果,任务完成之后自动会通过其他机制把结果传递给我们
                    
举例子:
	烧水案例: 普通水壶(水开了冒气)  响水壶(水开了,呜呜呜~)
        
    a.使用普通水壶烧水,烧水过程中我在旁边看着,别的事不能干!!! 【同步阻塞】
    b.使用普通水壶烧水,烧水过程中我去抽烟,抽根烟回来看一下,再抽根烟再回来看一下!! 【同步非阻塞】
    c.使用响水壶,烧水过程中直接去玩LOL,也不需要玩一局看一下,
								水开后水壶会以呜呜声音方式告诉我结果!!  【异步非阻塞】 
        		                 
```

##### 3.BIO,NIO,AIO的介绍

```java
BIO(传统的IO): 同步阻塞的IO
NIO(New新的IO): 同步阻塞的也可以是同步非阻塞,由Buffer(缓冲区),Channel(通道),Selector(选择器)  
NIO2(也叫AIO): 异步非阻塞的IO      
```

### 第三章 NIO-Buffer类(了解)

##### 1.Buffer的介绍和种类

- 什么是Buffer

  ```java
  Buffer称为缓冲区,本质就是一个数组
  ```

- Buffer的一般操作步骤

  ```java
  a.写入缓冲区(把数据保存到数组中)
  b.调用flip方法(切换缓冲区的写默写为读模式)
  c.读缓冲区(把数组中的数据读取出来)
  d.调用clear或者compact方法(清空缓冲区或者清除缓冲区中已经读取过的数据)    
  ```

- Buffer的种类

  ```java
  ByteBuffer 字节缓冲区(字节数组)【最常用】
  CharBuffer 字符缓冲区(字符数组)
  DoubleBuffer Double缓冲区(小数数组)
  FloatBuffer Float缓冲区(小数数组)
  IntBuffer 整型缓冲区(整型数组)
  LongBuffer 长整型缓冲区(长整型数组)
  ShortBuffer 短整型缓冲区(短整型数组)
  ```

##### 2.ByteBuffer的三种创建方式

```java
a.public static allocate(int capacity); 在堆区申请一个固定字节大小的ByteBuffer缓冲区
b.public static allocatDirect(int capacity);在系统的内存中申请一个固定字节大小的ByteBuffer缓冲区 c.public static wrap(byte[] arr);把一个字节数组直接包装成ByteBuffer缓冲区 
    
public class ByteBuffer01 {
    public static void main(String[] args) {
        //创建一个ByteBuffer
        //1.allocate
        ByteBuffer buffer1 = ByteBuffer.allocate(10); //在JVM的堆中,间接缓冲区
        //2.allocatDirect
        ByteBuffer buffer2 = ByteBuffer.allocateDirect(10); //直接和操作系统申请,直接缓冲区
        //创建和销毁角度来看, buffer1效率更高
        //操作缓冲区角度俩看, buffer2效率更好
        //3.wrap
        byte[] bs = new byte[10];
        ByteBuffer buffer3 = ByteBuffer.wrap(bs);
        //buffer3属于间接缓冲区
    }
}    
```

##### 3.ByteBuffer的三种添加数据方式

```java
a.public ByteBuffer put(byte b); 添加单个字节
b.public ByteBuffer put(byte[] bs);添加字节数组
c.public ByteBuffer put(byte[] bs,int startIndex,int len)：添加一个字节数组的一部分   
    
public class ByteBuffer02 {
    public static void main(String[] args) {
        //1.创建一个ByteBuffer
        ByteBuffer buffer = ByteBuffer.allocate(10);
        //2.打印
        System.out.println(Arrays.toString(buffer.array()));
        //3.添加数据
        //a.添加一个字节
        buffer.put((byte)10);
        buffer.put((byte)20);
        buffer.put((byte)30);
        System.out.println(Arrays.toString(buffer.array()));
        //b.添加一堆字节
        byte[] bs1 = {40,50,60};
        buffer.put(bs1);
        System.out.println(Arrays.toString(buffer.array()));
        //c.添加一堆字节的一部分
        byte[] bs2 = {70,80,90};
        buffer.put(bs2,1,2);
        System.out.println(Arrays.toString(buffer.array()));
    }
}    
```

##### 4.ByteBuffer的容量-capacity

```java
什么是容量(capacity):
	是指Buffer最多包含元素的个数,并且Buffer一旦创建容量无法更改
public int capacity(); 获取Buffer的容量

public class ByteBuffer03 {
    public static void main(String[] args) {
        //1.创建一个ByteBuffer
        ByteBuffer buffer = ByteBuffer.allocate(10);
        //2.获取容量
        int capacity = buffer.capacity();
        System.out.println("容量为:"+capacity);
    }
}        
```

##### 5.ByteBuffer的限制-limit

```java
什么是限制: 是指第一个不能操作的元素索引,限制的取值范围(0-capacity)   
限制作用: 相当于人为"修改"缓冲区的大小(实际上缓冲区大小没有改变,只是可访问的元素的个数变了)    
public class ByteBuffer04 {
    public static void main(String[] args) {
        //1.创建一个ByteBuffer
        ByteBuffer buffer = ByteBuffer.allocate(10);
        //2.打印
        System.out.println(Arrays.toString(buffer.array()));
        //3.获取限制
        System.out.println("当前缓冲区的限制:"+buffer.limit());
        //4.修改限制
        buffer.limit(3);
        System.out.println("将缓冲区的限制改为3");
        System.out.println(Arrays.toString(buffer.array()));
        //5.添加
        buffer.put((byte)10);
        buffer.put((byte)20);
        buffer.put((byte)30);
        buffer.put((byte)40);//索引是3
        System.out.println(Arrays.toString(buffer.array()));
    }
}  
```

##### 6.ByteBuffer的位置-position

```java
什么是位置: 将要写入/读取的元素的索引,位置取值范围(0-capacity/limit)
    
public int position(); 获取当前位置
public void positon(int newPosition);修改当的位置    

public class ByteBuffer05 {
    public static void main(String[] args) {
        //1.创建一个ByteBuffer
        ByteBuffer buffer = ByteBuffer.allocate(10);
        //2.获取数据
        System.out.println("当前容量:"+ buffer.capacity());
        System.out.println("当前限制:"+ buffer.limit());
        System.out.println("当前位置:"+ buffer.position());
        //3.添加数据
        buffer.put((byte)10); //0
        buffer.put((byte)20); //1
        buffer.put((byte)30); //2
        buffer.put((byte)40); //3
        System.out.println(Arrays.toString(buffer.array()));
        System.out.println("当前容量:"+ buffer.capacity());
        System.out.println("当前限制:"+ buffer.limit());
        System.out.println("当前位置:"+ buffer.position());
        //4.修改位置
        System.out.println("修改位置为2");
        buffer.position(2);
        //5.添加数据
        buffer.put((byte)50);
        System.out.println(Arrays.toString(buffer.array()));
        System.out.println("当前容量:"+ buffer.capacity());
        System.out.println("当前限制:"+ buffer.limit());
        System.out.println("当前位置:"+ buffer.position());
    }
}
    
```

##### 7.ByteBuffer的标记-mark

```java
什么是标记: 给当前的position记录下来,当调用reset(重置)时,position会回到标记,标记范围(0-position)
 
public class ByteBuffer06 {
    public static void main(String[] args) {
        //1.创建一个ByteBuffer
        ByteBuffer buffer = ByteBuffer.allocate(10);
        //2.添加数据
        buffer.put((byte)10);
        buffer.put((byte)20);
        buffer.put((byte)30);
        //position是 3
        //标记
        buffer.mark(); //记录当前位置 3
        buffer.put((byte)40);
        buffer.put((byte)50);
        buffer.put((byte)60);
        buffer.put((byte)70);
        buffer.put((byte)80);
        System.out.println(Arrays.toString(buffer.array()));
        //3.重置
        buffer.reset(); //把位置修改为刚刚的标记
        buffer.put((byte)99);
        buffer.put((byte)99);
        buffer.put((byte)99);
        System.out.println(Arrays.toString(buffer.array()));
    }
}    
```

##### 8. ByteBuffer的其他方法

```java
a.public int remaining()：获取position与limit之间的元素数。
b.public boolean isReadOnly()：获取当前缓冲区是否只读。  
c.public boolean isDirect();获取当前缓冲区是否为直接缓冲区。
d.public Buffer clear(); 清空缓冲区(还原缓冲区的状态) 
    将position设置为：0
    将限制limit设置为容量capacity
    并且会丢弃标记
e.public Buffer flip(); 切换读写模式(缩小范围)
    将limit设置为当前position位置 
    将当前position位置设置为0
    并且丢弃标记
f.public Buffer rewind();重绕此缓冲区。    
    将position位置设置为0
    限制limit不变
    丢弃标记  
```

### 第四章 Channel（通道）(了解)

##### 1. Channel介绍和分类

```java
什么是Channel: Channel是一个读写数据的类,和我们学的IO流类似,最大的不同在于IO流有Input和Output之分
    ,但是通道没有输入和输出通道之分,都叫Channel
通道(Channel的分类):
	FileChannel 文件通道,读写文件的
    DatagramChannel   UPD协议通道(通过UDP协议收发数据)
    SocketChannel    TCP协议中客户端的通道(给客户端读写数据用的)
    ServerSocketChannel TCP协议中服务器端通道(给服务器端读写数据用的)    
```

##### 2. FileChannel类的基本使用

```java
public class FileChannelDemo01 {
    public static void main(String[] args) throws IOException {
        //复制文件
        //1.创建文件对象
        File srcFile = new File("G:\\upload\\111.png"); //源文件
        File destFile = new File("copy.png");
        //2.创建文件的输入输出流
        FileInputStream fis = new FileInputStream(srcFile);
        FileOutputStream fos = new FileOutputStream(destFile);
        //3.通道
        FileChannel inChannel = fis.getChannel();
        FileChannel outChannel = fos.getChannel();
        //4.复制文件
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        int len = 0;
        while ((len = inChannel.read(buffer)) != -1) {
            //切换模式
            buffer.flip();
            //读缓冲区数据,写入到文件中
            outChannel.write(buffer);
            //清空
            buffer.clear();
        }
        //5.是否资源
        outChannel.close();
        inChannel.close();
        fos.close();
        fis.close();
    }
}
```

##### 3. FileChannel结合MappedByteBuffer实现高效读写

![image-20200326120241468](img/image-20200326120241468.png)

```java
public class FileChannelDemo02 {
    public static void main(String[] args) throws Exception {
        //1.创建两个文件
        // 只读模式 r
        // 读写模式 rw
        RandomAccessFile srcFile = new RandomAccessFile("H:\\BaiduNetdiskDownload\\cxf_web\\day03.zip", "r");
        RandomAccessFile destFile = new RandomAccessFile("copy.zip", "rw");
        //2.获取通道
        FileChannel inChannel = srcFile.getChannel();
        FileChannel outChannel = destFile.getChannel();
        //3.获取文件的大小
        int size = (int) inChannel.size();//
        //4.建立映射字节缓冲区
        //map(模式,开始索引,字节数);
        MappedByteBuffer inMap = inChannel.map(FileChannel.MapMode.READ_ONLY, 0, size);
        MappedByteBuffer outMap = outChannel.map(FileChannel.MapMode.READ_WRITE, 0, size);
        //5.复制 耗时:10949毫秒
        long start = System.currentTimeMillis();
        for (int i = 0; i < size; i++) {
            byte b = inMap.get(i);
            outMap.put(i, b);
        }
        long end = System.currentTimeMillis();
        System.out.println("耗时:"+(end-start)+"毫秒");
        //6.释放资源
        outChannel.close();
        inChannel.close();
    }
}

注意: 
	a.MappedByteBuffer只适用于复制2G以下的文件
    b.如果是2G以上文件,分多次复制(参考案例:复制2GB以上文件)       
```

##### 4. SocketChannel和ServerSocketChannel的实现连接

- ServerSocketChannel的创建(阻塞方式)

  ```java
  //创建阻塞的服务器通道
  public class ServerSocketChannelDemo {
      public static void main(String[] args) throws IOException {
          //1.创建ServerSocketChannel
          ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
          //2.绑定本地的某个端口
          serverSocketChannel.bind(new InetSocketAddress(8888));
          System.out.println("服务器已经启动...");
          //3.接收客户端通道
          SocketChannel socketChannel = serverSocketChannel.accept();
          //4.后续代码
          System.out.println("后续代码...");
      }
  }
  ```

- ServerSocketChannel的创建(非阻塞方式)

  ```java
  /**
   * 同步非阻塞的服务器通道..
   */
  public class ServerSocketChannelDemo02 {
      public static void main(String[] args) throws IOException, InterruptedException {
          //1.创建ServerSocketChannel
          ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
          //设置为同步非阻塞的服务器通道
          serverSocketChannel.configureBlocking(false);
          //2.绑定本地的某个端口
          serverSocketChannel.bind(new InetSocketAddress(8888));
          System.out.println("服务器已经启动...");
          while (true) {
              //3.接收客户端通道
              SocketChannel socketChannel = serverSocketChannel.accept();
              //4.后续代码
              System.out.println("后续代码...");
              //5.判断
              if (socketChannel != null) {
                  System.out.println("和客户端进行交互...");
              }else{
                  System.out.println("暂时没有客户端,2秒后继续查看...");
                  Thread.sleep(2000); //模拟服务器去做其他任务
              }
          }
      }
  }
  ```

- SocketChannel的创建

  ```java
  //阻塞式的客户端
  public class SocketChannelDemo01 {
      public static void main(String[] args) throws IOException {
          //1.创建SocketChannel对象
          SocketChannel socketChannel = SocketChannel.open();
          //2.去连接服务器
          boolean b = socketChannel.connect(new InetSocketAddress("127.0.0.1", 8888));
          //相当于以前 Socket socket = new Socket("127.0.0.1",8888);
          //3.后续代码
          System.out.println("后续代码...");
      }
  }
  
  
  /**
   * 非阻塞式的客户端
   */
  public class SocketChannelDemo02 {
      public static void main(String[] args) throws InterruptedException, IOException {
          //1.创建SocketChannel对象
          SocketChannel socketChannel = SocketChannel.open();
          //设置,设置为非阻塞的
          socketChannel.configureBlocking(false);
          while (true) {
              //2.去连接服务器
              try {
                  boolean b = socketChannel.connect(new InetSocketAddress("127.0.0.1", 8888));
                  //相当于以前 Socket socket = new Socket("127.0.0.1",8888);
                  //3.后续代码
                  System.out.println("后续代码...");
                  //4.判断
                  if (b) {
                      System.out.println("和服务器进行交互...");
                  }
              }catch (Exception e){
                  System.out.println("两秒后重写连接...");
                  Thread.sleep(2000);
              }
          }
      }
  }
  ```

##### 5. SocketChannel和ServerSocketChannel的实现通信

```java
public class ServerSocketChannelDemo {
    public static void main(String[] args) throws IOException {
        //1.创建ServerSocketChannel对象
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(9999));
        //2.接收客户端
        SocketChannel socketChannel = serverSocketChannel.accept();
        System.out.println("有客户端连接了...");
        //3.读取数据
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        int len = socketChannel.read(byteBuffer);
        //4.打印数据
        byteBuffer.flip(); //先把byteBuffer切换为读模式
        String str = new String(byteBuffer.array(), 0, len);
        System.out.println("客户端说:"+str);
        //5.回数据
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        buffer.put("Hello,我是服务器..".getBytes());
        //切换为读模式
        buffer.flip();
        socketChannel.write(buffer);
        //6.释放资源
        socketChannel.close();
        serverSocketChannel.close();
    }
}

public class SocketChannelDemo {
    public static void main(String[] args) throws IOException {
        //1,创建SocketChannel对象
        SocketChannel socketChannel = SocketChannel.open();
        //2.去连接
        boolean b = socketChannel.connect(new InetSocketAddress("127.0.0.1", 9999));
        //3.判断
        if (b) {
            //4.发送数据
            System.out.println("连接服务器成功....");
            ByteBuffer byteBuffer = ByteBuffer.wrap("Hello,我是客户端...".getBytes());
            socketChannel.write(byteBuffer);
            //5.读取数据
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            int len = socketChannel.read(buffer);
            buffer.flip();
            System.out.println(new String(buffer.array(),0,len));
            //6.释放资源
            socketChannel.close();
        }
    }
}
```

##### 总结

```java
能够使用Junit进行单元测试【重点】
    a.Junit是第三方的单元测试框架
    b.@Test 将一个普通方法可以独立运行
    c.@Before @After @BeforeClass @AfterClass
    
能够说出阻塞和非阻塞的概念
    阻塞: 任务没有执行完毕,线程无法向下继续执行
    非阻塞: 无论任务是否执行完毕,线程继续向下执行    
能够说出同步和异步的概念
    同步,可以是阻塞的,可以非阻塞
        同步阻塞的,任务没有执行完毕,线程无法向下继续执行
        同步非阻塞,无论任务是否执行完毕,线程继续向下执行,后期我们需要自己去写代码来获取任务的结果
	异步,一般都是非阻塞的
        异步非阻塞,无论任务是否执行完毕,线程继续向下执行.后期任务执行完毕会想办法通知我们
能够创建和使用ByteBuffer
   创建:
		allocate(字节数); //JVM的堆中申请的空间,间接
		allocatDirect(字节数); // 系统的内存中申请的空间,直接
		wrap(字节数组); // JVM的堆中申请的空间,间接
	使用:
		put(字节/字节数组/字节数组的一部分);
		capacity,limit,position,mark,reset,rewind,flip,clear
		
能够使用MappedByteBuffer实现高效读写
     使用文件对象 RandomAccessFile 不能使用普通的File
            
能够使用ServerSocketChannel和SocketChannel实现连接并收发信息
     ServerSocketChannel: 服务器端,可以是同步阻塞的也可以是同步非阻塞的
     SocketChannel: 客户端,可以是同步阻塞的也可以是同步非阻塞的
     通过configureBlocking 方式可以设置阻塞还是非阻塞    
```



