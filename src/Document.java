import java.awt.*;
import java.io.Serializable;

public class Document implements Serializable {
    private String title;
    private String type;
    private String detail;
    private Point loc;
    private transient DocumentDetailView view;

    Document() {
        this("", "", "");
    }

    Document(String title, String type, String detail) {
        setTitle(title);
        setType(type);
        setDetail(detail);
    }

    public String getTitle() {
        return title;
    }

    public String getType() {
        return type;
    }

    public String getDetail() {
        return detail;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public DocumentDetailView getView() {
        if (view == null) view = new DocumentDetailView(this);
        return view;
    }

    public void setLoc(Point loc) {
        this.loc = loc;
    }

    public Point getLoc() {
        return loc;
    }
}
