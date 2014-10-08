package demo.jaxrs.server;
import javax.xml.bind.annotation.*;

/**
 * Created by prindu on 08/10/14.
 */
@XmlType
public class Library {
    private int content_id,category_id;
    private String title,url,published_date;

    public int getContent_id(){return content_id;}
    public void setContent_id(int content_id){this.content_id=content_id;}

    public int getCategory_id(){return category_id;}
    public  void setCategory_id(int category_id){this.category_id=category_id;}

    public String getTitle(){return title;}
    public void setTitle(String title){this.title=title;}

    public String getUrl(){return url;}
    private void setUrl(String url){this.url=url;}

    public String getPublished_date(){return  published_date;}
    public void setPublished_date(String published_date){this.published_date=published_date;}


    @Override public String toString() {
        return "Content: id=" + getContent_id() + ", Title=" + getTitle();
    }


}



