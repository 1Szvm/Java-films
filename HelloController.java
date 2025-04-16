package com.example.diafilmgui;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.TreeSet;

public class HelloController {
    @FXML private ListView<String> lsFilmek;
    @FXML private ComboBox<String> cmEvek;
    @FXML private Label filmDB;
    @FXML public CheckBox cbBlackWhite;
    @FXML public CheckBox cbColored;

    private FileChooser fc = new FileChooser();

    public void initialize(){
        fc.setInitialDirectory(new File("./"));
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Fájlok","*.csv"));
    }

    @FXML private void onMegnyitasClick(){
        File fbe=fc.showOpenDialog(lsFilmek.getScene().getWindow());
        if(fbe!=null){
            list.clear();
            lsFilmek.getItems().clear();
            betolt(fbe);
            TreeSet<Integer> evek = new TreeSet<>();
            for (Film e: list){
                lsFilmek.getItems().add(String.format("%s (%d, %d kocka, %s)",e.cim,e.ev,e.fps,e.szines));
                evek.add(e.ev);
            }
            for(Integer ev:evek){
                cmEvek.getItems().add(ev.toString());
            }
            cmEvek.getSelectionModel().select(0);
        }
    }

    @FXML private void renderList(){
        int db=0;
        lsFilmek.getItems().clear();
        for (Film e:list){
            if(e.ev==Integer.parseInt(cmEvek.getSelectionModel().getSelectedItem().split(" ")[0])){
                if (cbBlackWhite.isSelected()&&e.szines.equals("N")) {
                    lsFilmek.getItems().add(String.format("%s (%d, %d kocka, fekete-fehér)",e.cim,e.ev,e.fps));
                } else if (cbColored.isSelected()&&e.szines.equals("I")) {
                    lsFilmek.getItems().add(String.format("%s (%d, %d kocka, színes)",e.cim,e.ev,e.fps));
                }
                db++;
            }
        }
        filmDB.setText(String.format("%d darab",db));
    }


    private void betolt(File Fajl){
        Scanner be = null;
        try{
            be= new Scanner(Fajl,"utf-8");
            be.nextLine();
            while (be.hasNextLine()){
                list.add(new Film(be.nextLine()));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }finally {
            if(be != null) be.close();
        }
    }

    public class Film{
        String cim;
        Integer ev;
        Integer fps;
        String szines;

        public Film(String sor){
            String[] s =sor.split(";");
            cim=s[0];
            ev=Integer.parseInt(s[1]);
            fps=Integer.parseInt(s[2]);
            szines=s[3];
        }
    }

    public ArrayList<Film> list = new ArrayList<>();

    @FXML private void onKilépesClick(){
        Platform.exit();
    }

    @FXML private void onNevjegyzekClikc(){
        Alert info = new Alert(Alert.AlertType.INFORMATION);
        info.setTitle("Névjegy");
        info.setHeaderText(null);
        info.setContentText("Diafilm v1.0.0\n(C) Kandó");
        info.showAndWait();
    }
}