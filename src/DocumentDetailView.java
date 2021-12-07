import javax.swing.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class DocumentDetailView {
    private final JFrame frame;
    private final JTextArea TITLE_FIELD;
    private final Document document;

    DocumentDetailView(Document document) {
        frame = new JFrame();
        TITLE_FIELD = new JTextArea();
        TITLE_FIELD.setRows(5);
        TITLE_FIELD.setColumns(15);
        TITLE_FIELD.setEditable(false);

        this.document = document;

        frame.add(TITLE_FIELD);
        frame.pack();
        frame.setResizable(true);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentMoved(ComponentEvent e) {
                super.componentMoved(e);
                document.setLoc(e.getComponent().getLocation());
            }
        });
        update();
    }

    public void show() {
        if (document.getLoc() != null) {
            frame.setLocation(document.getLoc());
        }
        frame.setVisible(true);
    }

    public void update() {
        TITLE_FIELD.setText(getText());
    }

    public void close() {
        frame.dispose();
    }

    private String getText() {
        String str = "Title : " + document.getTitle() + "\n";
        str += "Type : " + document.getType() + "\n";
        str += "Detail : " + document.getDetail();
        return str;
    }
}
