package org.lipeng.demo.collectioncomments;

public class MyMain
{
  public static void main(String[] args) 
  {
     Singleton obj = Singleton.getInstance();                                                                    
     System.out.println("obj.counter1=" +obj.counter1);
     System.out.println("obj.counter2=" +obj.counter2);
   }
}
