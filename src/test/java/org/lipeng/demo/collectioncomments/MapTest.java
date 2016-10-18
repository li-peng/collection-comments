package org.lipeng.demo.collectioncomments;

import org.junit.Test;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * @author lipeng
 * @date 2016/10/13
 */
public class MapTest {
    /**
     * 测试key的equals和hashCode不一致会导致怎样的后果
     * equals ==  ，而 hashcode<> ：会保存两个相同(equals)的key
     * 而
     * equals <> ，而 hashcode== : 会造成hash冲突，如果HashMap中存放的所有值，equals不同，但是hashCode，相同，
     * 那么这个hashMap其实就是一个链表，即table数组只有一个位置有值，并存放一个链表
     */
    @Test
    public void test() {
        Map<User, Integer> map = new HashMap7<>();
        User u1 = new User(1, "张三");
        User u2 = new User(2, "李四");
        User u3 = new User(3, "王五");
        User u4 = new User(4, "赵六");
//        map.put(u1,1);
        map.put(u1, 1);
        map.put(u2, 2);
        map.put(u3, 3);
        map.put(u4, 4);

        for (Map.Entry<User, Integer> entry : map.entrySet()) {
            User key = entry.getKey();
            Integer value = entry.getValue();
            System.out.println(key.getName());

        }
    }

    /**
     * 测试keyset
     */
    @Test
    public void test1() {
        Map<Integer, Integer> map = new HashMap7<>();
        map.put(1, 1);
        map.put(2, 2);
        map.put(3, 3);
        map.put(4, 4);
        Set<Integer> keySet = map.keySet();
        // keySet.add(22); error:throw UnsupportedOperationException
        map.put(5,5);
        System.out.println(keySet);//[1, 2, 3, 4, 5] 说明keySet只是一个视图，map对key的增加和删除会同步到keySet中
        Iterator<Integer> it = keySet.iterator();
        map.put(6,6);
        while(it.hasNext()){
            System.out.println(it.next());// error: throw ConcurrentModificationException 获取it后，map.put 方法修改了modCount 造成此异常
        }
        System.out.println("1111111111111");
    }

    @Test
    public void test2() {
        Map<Integer, Integer> map = new LinkedHashMap7<>();
        map.put(1, 1);
        map.put(32, 2);
        map.put(13, 3);
        map.put(4, 4);
        Iterator<Integer> it=map.keySet().iterator();
        System.out.println("111111111111111111111");

    }


}
