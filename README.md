# off-heap-demo
The demo project for Java GC, Off-heap workshop

http://www.slideshare.net/ValeriyMoiseenko/java-gc-offheap-workshop/ValeriyMoiseenko/java-gc-offheap-workshop

Examples of use:

java -XX:+PrintGCDetails -XX:+PrintGCDateStamps -Xloggc:gc_linkedlist.log -jar off-heap-demo.jar linkedList

java -XX:+PrintGCDetails -XX:+PrintGCDateStamps -Xloggc:gc_arraylist.log -jar off-heap-demo.jar arrayList

java -XX:+PrintGCDetails -XX:+PrintGCDateStamps -Xloggc:gc_bytebuffer.log -jar off-heap-demo.jar byteBuffer

java -XX:+PrintGCDetails -XX:+PrintGCDateStamps -Xloggc:gc_directbytebbuffer.log -jar off-heap-demo.jar directByteBuffer

java -XX:+PrintGCDetails -XX:+PrintGCDateStamps -Xloggc:gc_unsafe.log -jar off-heap-demo.jar unsafe

java -XX:+PrintGCDetails -XX:+PrintGCDateStamps -Xloggc:gc_mapdb.log -jar off-heap-demo.jar mapdb
