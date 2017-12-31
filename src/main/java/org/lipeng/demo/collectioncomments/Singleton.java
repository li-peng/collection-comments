package org.lipeng.demo.collectioncomments;

class Singleton {
    private static Singleton obj
            = new Singleton();
    public static int counter1;
    public static int counter2 = 0;

    private Singleton() {
        counter1++;
        counter2++;
    }

    public static Singleton getInstance() {
        return obj;
    }
}
