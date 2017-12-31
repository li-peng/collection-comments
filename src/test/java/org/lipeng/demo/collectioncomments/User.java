package org.lipeng.demo.collectioncomments;

/**
 * @author lipeng
 * @date 2016/10/15
 */
public class User {
    private Integer id;
    private String name;
    private Integer gender;
    public User(){

    }
    public User(Integer id,String name){
        this.id=id;
        this.name=name;
    }
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    @Override
    public int hashCode() {
     //   return super.hashCode();
        return 1;
    }
    @Override
    public boolean equals(Object obj) {
      //  return true;
        return super.equals(obj);
    }

    public Integer getGender() {
        return gender;
    }

    public User setGender(Integer gender) {
        this.gender = gender;
        return this;
    }
}
