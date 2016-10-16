package org.lipeng.demo.collectioncomments;

import org.junit.Test;

import java.util.*;

public class ListTest  {
    private Integer id;
    private String name;

    @Test
    public void testName() throws Exception {
        ArrayList<Integer> list = new ArrayList<Integer>();
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(3);
        list.remove(new Integer(1));
        List<Integer> cloneList = (List<Integer>) list.clone();
        System.out.println(cloneList.size());
        LinkedList<String> linkedList = new LinkedList<String>();

        Map<String, String> map = new HashMap<>();


    }

    public void function() {
        String zahngsan = String.format("your name:%s" + "zhangsan");
    }

}
