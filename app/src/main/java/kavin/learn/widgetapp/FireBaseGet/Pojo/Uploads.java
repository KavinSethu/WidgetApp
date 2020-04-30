package kavin.learn.widgetapp.FireBaseGet.Pojo;

import java.io.Serializable;

/**
 * Created by Kavin on 11/14/2019.
 */
public class Uploads implements Serializable {

    public String age;

    public String name;

    public String image;

    public Uploads() {

    }

    public Uploads(String age, String name, String image) {

        this.age = age;
        this.name = name;
        this.image = image;
    }

    public String getAge() {

        return age;
    }

    public void setAge(String age) {

        this.age = age;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {

        this.name = name;
    }

    public String getImage() {

        return image;
    }

    public void setImage(String image) {

        this.image = image;
    }
}
