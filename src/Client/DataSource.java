package Client;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Created by miral on 16/03/17.
 */
public class DataSource {
    private  ObservableList<FileList> files = FXCollections.observableArrayList();

    public ObservableList<FileList> getFiles() { return files; }

    public void setFiles(String fileName) { files.add(new FileList(fileName)); }

}
