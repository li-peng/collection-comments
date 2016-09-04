package org.lipeng.demo.collectioncomments;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import org.junit.Test;

public class ListTest {
	@Test
	public void testName() throws Exception {
		ArrayList<Integer> list=new ArrayList<Integer>();
		list.add(1);
		list.add(2);
		list.add(3);
		list.add(3);
		list.remove(new Integer(1));
		List<Integer> cloneList=(List<Integer>) list.clone();
		System.out.println(cloneList.size());
		LinkedList<String> linkedList=new LinkedList<String>();
	}
}
