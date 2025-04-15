import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.TreeSet;

public class Main {
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

    public Main(){
        betolt("diafilm.csv");
        //0.feladat
        System.out.printf("0) %d diafilm adata beolvasva\n",list.size());

        int colored=0;
        for(Film film:list){
            if (!film.szines.equals("I")){
               colored++;
            }
        }
        System.out.printf("   Közülük %d még fekete-fehér\n",colored);

        //1.feladat
        int oldest=list.get(0).ev;
        for(Film film:list){
            if (oldest>film.ev) oldest= film.ev;
        }

        ArrayList<Film> oldestMovies= new ArrayList<>();
        for(Film film:list){
            if (oldest==film.ev) oldestMovies.add(film);
        }

        System.out.printf("1) A legrégebbi diafilm: %s (%d)\n",oldestMovies.get(0).cim,oldestMovies.get(0).ev);
        if(oldestMovies.size()>1){
            System.out.println("   De ugyanebben az évben készült még:");
            for (int i=1;i<oldestMovies.size();i++){
                System.out.printf("   - %s (%d)\n",oldestMovies.get(i).cim,oldestMovies.get(i).ev);
            }
        }

        //2.feladat
        double sumBefor2000=0;
        double sumAfter2000=0;
        int count=0;

        for(Film film:list){
            if (film.ev<2000){
                sumBefor2000+=film.fps;
                count++;
            }else{
                sumAfter2000+=film.fps;
            }
        }

        System.out.printf("2) A 2000 előtt készült diafilmek átlagos kockaszáma: %.1f\n",(sumBefor2000/count));
        System.out.printf("   A később készült diafilmeknél az áltag: %.1f\n",(sumAfter2000/(list.size()-count)));

        //3.faladat
        System.out.println("3) Az egyes évtizedekben készült diafilmek száma:");
        TreeMap<Integer,Integer> evSum= new TreeMap<>();
        for(Film film:list){
            int decade=(film.ev-Integer.parseInt(film.ev.toString().substring(3,4)));
            if(!evSum.containsKey(decade)){
                evSum.put(decade,1);
            }else{
                evSum.put(decade,evSum.get(decade)+1);
            }
        }

        for (Integer dec:evSum.keySet()){
            System.out.printf("   %d-%d : %d darab\n",dec,dec+9,evSum.get(dec));
        }

        //4.feladat
        TreeMap<Integer,Integer> fpsSum= new TreeMap<>();
        for(Film film:list){
            if(!fpsSum.containsKey(film.ev)){
                fpsSum.put(film.ev, film.fps);
            }else{
                fpsSum.put(film.ev,fpsSum.get(film.ev)+ film.fps);
            }
        }

        TreeSet <Integer> sortedFpsSum= new TreeSet<>();
        for (Integer ev:fpsSum.keySet()){
            sortedFpsSum.add(fpsSum.get(ev));
        }
        int maxYear=sortedFpsSum.last();
        sortedFpsSum.removeLast();
        int max2thYear=sortedFpsSum.last();

        for (Integer ev:fpsSum.keySet()){
            if(fpsSum.get(ev)==maxYear) System.out.printf("4) A legtöbb kocka (%d db) készítésének éve: %d\n",fpsSum.get(ev),ev);
            if(fpsSum.get(ev)==max2thYear) System.out.printf("   A második legtöbb kocka (%d db) éve: %d\n",fpsSum.get(ev),ev);
        }

        //5.feladat
        PrintWriter ki = null;
        try{
            ki = new PrintWriter(new File("200x.txt"),"utf-8");
            for(Film film:list){
                if(film.ev>=2000) {
                    ki.printf("%s;%d;%d;%s\r\n",film.cim,film.ev,film.fps,film.szines);
                };
            }
        }catch (UnsupportedEncodingException e){
            System.out.println(e);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            if(ki !=null) ki.close();
        }
        System.out.printf("5) A 200x évben megjelent diák adatai elmentve (200x.txt)");
    }

    private void betolt(String FajNev){
        Scanner be = null;
        try{
            be= new Scanner(new File(FajNev),"utf-8");
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

    public static void main(String[] args) {
        new Main();
    }
}