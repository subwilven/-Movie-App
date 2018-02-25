package com.example.android.movieapp.POJO;

/**
 * Created by eslam on 21-Feb-18.
 */

public class Video {
    String key ;
    String id ;
    String name;
    String size;
    String type;


    public void setKey(String key)
    {
        this.key=key;

    }

    public void setId(String id)
    {
        this.id=id;

    }
    public void setName(String name)
    {
        this.name=name;

    }

    public void setSize(String size)
    {
        this.size=size;

    }

    public void setType(String type)
    {
        this.type=type;

    }

    public String getKey()
    {
        return key;
    }
    public String getId()
    {
        return id;
    }
    public String getName()
    {
        return name;
    }
    public String getSize()
    {
        return size;
    }
    public String getType()
    {
        return type;
    }

}
