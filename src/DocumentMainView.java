import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.util.ArrayList;

public class DocumentMainView {

    private ArrayList<Document> documents;
    private int currentIndex = -1;
    private final JTextField TITLE_FIELD, INDEX_FIELD;
    private final JTextArea DETAIL_FIELD;
    private final JComboBox<String> TYPE_COMBO;

    DocumentMainView() {
        JFrame frame = new JFrame();
        documents = new ArrayList<>();
        TITLE_FIELD = new JTextField();
        INDEX_FIELD = new JTextField();
        DETAIL_FIELD = new JTextArea();
        TYPE_COMBO = new JComboBox<>(new String[]{
                "Normal",
                "Formal",
                "Informal",
                "etc"
        });

        INDEX_FIELD.setEditable(false);
        TITLE_FIELD.setColumns(20);
        DETAIL_FIELD.setRows(5);

        JButton previousBtn = new JButton("Previous");
        JButton nextBtn = new JButton("Next");
        JButton insertBtn = new JButton("Add");
        JButton updateBtn = new JButton("Update");
        JButton delBtn = new JButton("Delete");
        insertBtn.addActionListener(this::insertAction);
        updateBtn.addActionListener(this::updateAction);
        delBtn.addActionListener(this::deleteAction);
        nextBtn.addActionListener(this::nextPageAction);
        previousBtn.addActionListener(this::previousPageAction);

        JPanel headerPanel = new JPanel(new BorderLayout());
        JPanel titlePanel = new JPanel(new FlowLayout());
        JPanel centerPanel = new JPanel(new BorderLayout());
        JPanel typePanel = new JPanel(new FlowLayout());
        JPanel footerPanel = new JPanel(new GridLayout(2, 1));
        JPanel indexPanel = new JPanel(new FlowLayout());
        JPanel actionPanel = new JPanel(new FlowLayout());
        JPanel detailPanel = new JPanel(new BorderLayout());

        indexPanel.add(previousBtn);
        indexPanel.add(INDEX_FIELD);
        indexPanel.add(nextBtn);

        actionPanel.add(insertBtn);
        actionPanel.add(updateBtn);
        actionPanel.add(delBtn);

        titlePanel.add(new JLabel("Title"));
        titlePanel.add(TITLE_FIELD);

        typePanel.add(new JLabel("Type"));
        typePanel.add(TYPE_COMBO);

        headerPanel.add(titlePanel, BorderLayout.NORTH);
        headerPanel.add(typePanel, BorderLayout.SOUTH);

        footerPanel.add(indexPanel);
        footerPanel.add(actionPanel);

        detailPanel.add(new JLabel("Detail"), BorderLayout.NORTH);
        detailPanel.add(DETAIL_FIELD, BorderLayout.CENTER);

        centerPanel.add(detailPanel, BorderLayout.CENTER);
        centerPanel.add(new JLabel("    "), BorderLayout.EAST);
        centerPanel.add(new JLabel("    "), BorderLayout.WEST);

        frame.getContentPane().add(headerPanel, BorderLayout.NORTH);
        frame.getContentPane().add(footerPanel, BorderLayout.SOUTH);
        frame.getContentPane().add(centerPanel);

        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        frame.addWindowListener(new WindowAdapter() {
            public void windowOpened(WindowEvent e) {
                super.windowOpened(e);
                loadData();
            }

            @Override
            public void windowClosing(WindowEvent e) {
                saveData();
            }
        });
    }

    public void loadData() {
        File f = new File("Documents.data");
        if (f.exists()) {
            try(FileInputStream in = new FileInputStream(f)) {
                ObjectInputStream ois = new ObjectInputStream(in);
                documents = (ArrayList<Document>) ois.readObject();
            } catch (ClassNotFoundException classNotFoundException) {
                documents = new ArrayList<>();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        } else {
            documents = new ArrayList<>();
        }
        documents.forEach(doc -> {
            doc.getView().show();
        });
        if (documents.size() > 0) {
            currentIndex = 0;
        }

        updateFormField();
    }
    public void saveData() {
        try(FileOutputStream out = new FileOutputStream("Documents.data")) {
            ObjectOutputStream oos = new ObjectOutputStream(out);
            oos.writeObject(documents);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
    public void updateFormField() {
        Document doc = new Document();
        if (documents.size() > 0 && currentIndex != -1) {
            doc = documents.get(currentIndex);
        }
        TITLE_FIELD.setText(doc.getTitle());
        DETAIL_FIELD.setText(""+doc.getDetail());
        TYPE_COMBO.setSelectedItem(doc.getType());
        INDEX_FIELD.setText(""+(currentIndex + 1));
    }
    private void insertAction(ActionEvent e) {
        Document doc = new Document(
                TITLE_FIELD.getText(),
                (String) TYPE_COMBO.getSelectedItem(),
                DETAIL_FIELD.getText()
        );
        documents.add(doc);
        currentIndex = documents.size() - 1;
        updateFormField();
        saveData();
        doc.getView().show();
    }
    private void updateAction(ActionEvent e) {
        if (currentIndex == -1) {
            return;
        }
        Document doc = documents.get(currentIndex);
        doc.setTitle(TITLE_FIELD.getText());
        doc.setDetail(DETAIL_FIELD.getText());
        doc.setType((String) TYPE_COMBO.getSelectedItem());
        doc.getView().update();
        documents.set(currentIndex, doc);
        saveData();
    }
    private void deleteAction(ActionEvent e) {
        if (currentIndex == -1 || documents.size() < 1) {
            return;
        }
        Document doc = documents.get(currentIndex);
        doc.getView().close();
        documents.remove(currentIndex);
        currentIndex--;
        if (currentIndex == -1 && documents.size() > 0) {
            currentIndex++;
        }
        updateFormField();
        saveData();
    }
    private void nextPageAction(ActionEvent e) {
        if (currentIndex < documents.size() - 1) {
            currentIndex++;
            updateFormField();
        }
    }
    private void previousPageAction(ActionEvent e) {
        if (currentIndex > 0) {
            currentIndex--;
            updateFormField();
        }
    }
}
